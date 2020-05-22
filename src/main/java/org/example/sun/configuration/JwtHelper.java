package org.example.sun.configuration;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.util.CollectionUtils;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 * @author luoluo
 * @email 15360801546@163.com
 * @version 1.0
 * @date 创建时间：2018-1-16 16:35:17
 */
public final class JwtHelper {

//    private static final String KEY = StringUtilsEx.getUUID();
    private static final String KEY = "this is a json web token private key to encode or decode";

    /**
     * 用于记录用户更新token的次数，修改密码/用户状态变更时，使用
     */
//    private static final Map<String, AtomicInteger> CACHE = new ConcurrentHashMap<>();

    public static Claims parseToken(String jsonWebToken) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(KEY))
                    .parseClaimsJws(jsonWebToken).getBody();
            if (claims != null) {
                Object userid = claims.get("userId");
                Integer platform = -1;
                if (claims.get("platform") != null) {
                    platform = Integer.valueOf(claims.get("platform").toString());
                }
                
//                String cacheIdStr = RedisUtils.get(userid.toString() + platform);
//                if (Strings.isNullOrEmpty(cacheIdStr)) { // redis中没有用户信息，可能被禁用，或者密码被修改等
//                    return null;
//                }
//                if (userid != null && platform == SysOperator.APP_LOGIN && claims.containsKey("cacheId")) {
//                    Integer currentId = Integer.valueOf(claims.get("cacheId").toString());
//                    Integer cacheId = Integer.valueOf(cacheIdStr);
//                    // 移动端不允许多地登陆，pc端允许多地登陆
//                    if( (cacheId == null || cacheId <= 0 || !currentId.equals(cacheId)) ) { // APP用户身份已过期
//                        return null;
//                    }
//                }
            }
            return claims;
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException | UnsupportedJwtException | IllegalArgumentException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String createToken(Map<String, Object> tokenData, long TTLMillis) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //生成签名密钥  
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder() //
                .setHeaderParam("type", "JWT");
        String userId = null;
        Integer platform = null;
        if (tokenData != null && !tokenData.isEmpty()) {
            tokenData.forEach((k, v) -> builder.claim(k, v));
            userId = tokenData.get("userId").toString();
        }

        
        builder.claim("logintime", System.currentTimeMillis())//登录时间，确保每次生成的jwt不一样
        .signWith(signatureAlgorithm, signingKey);
        //添加Token过期时间  
        if (TTLMillis >= 0) {
            long expMillis = nowMillis + TTLMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp).setNotBefore(now);
        }

        return builder.compact();
    }
    
    @Deprecated
    public static String createToken(String userId, long TTLMillis) {
        
        return createToken(new HashMap(){{
            put("userId", userId);
        }}, TTLMillis);
    }
    
    /**
     * 为指定平台创建 token，如 app、小程序等
     *
     * @param platform
     * @param token_params
     * @param expired_time_mills
     * @return
     */
    public static String createTokenForSpecificPlatform(String platform, Map<String, Object> token_params, long expired_time_mills) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().setHeaderParam("type", "JWT").claim("platform", platform);

        if (CollectionUtils.isEmpty(token_params) == false) {
            for (String key : token_params.keySet()) {
                builder = builder.claim(key, token_params.get(key));
            }
        }

        builder.signWith(signatureAlgorithm, signingKey);
        //添加Token过期时间
        if (expired_time_mills >= 0) {
            long expMillis = nowMillis + expired_time_mills;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp).setNotBefore(now);
        }

        //生成JWT
        return builder.compact();
    }


    

}
