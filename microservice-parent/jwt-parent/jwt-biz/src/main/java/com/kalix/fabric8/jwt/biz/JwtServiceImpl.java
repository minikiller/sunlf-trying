package com.kalix.fabric8.jwt.biz;

import com.kalix.fabric8.jwt.api.biz.IJwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

public class JwtServiceImpl implements IJwtService {
    private final String PrivateKey_File = "jwtRS256.key";
    private final String PublicKey_File = "jwtRS256.key.pub";


    @Override
    public String getPrivateKeyString() {
        return getKeyString(PrivateKey_File);
    }

    @Override
    public String getPublicKeyString() {
        return getKeyString(PublicKey_File);
    }

    private String getKeyString(String keyFile) {
        String keyStr = FileUtil.loadFile(keyFile);
        if (keyStr != null) {
            keyStr = keyStr.replaceAll("\r", "");
            keyStr = keyStr.trim();
        }
        return keyStr;
    }

    /**
     * 获取PrivateKey对象
     * @param privateKeyBase64
     * @return
     */
    private PrivateKey getPrivateKey(String privateKeyBase64) {
        String privKeyPEM = privateKeyBase64.replaceAll("-----END RSA PRIVATE KEY-----", "")
                .replaceAll("-----BEGIN RSA PRIVATE KEY-----", "")
                .replaceAll("\n", "");
        byte[] encoded = org.apache.commons.codec.binary.Base64.decodeBase64(privKeyPEM);
        try {
            DerInputStream derReader = new DerInputStream(encoded);
            DerValue[] seq = derReader.getSequence(0);
            if (seq.length < 9) {
                throw new GeneralSecurityException("Could not read private key");
            }
            // skip version seq[0];
            BigInteger modulus = seq[1].getBigInteger();
            BigInteger publicExp = seq[2].getBigInteger();
            BigInteger privateExp = seq[3].getBigInteger();
            BigInteger primeP = seq[4].getBigInteger();
            BigInteger primeQ = seq[5].getBigInteger();
            BigInteger expP = seq[6].getBigInteger();
            BigInteger expQ = seq[7].getBigInteger();
            BigInteger crtCoeff = seq[8].getBigInteger();

            RSAPrivateCrtKeySpec keySpec = new RSAPrivateCrtKeySpec(modulus, publicExp, privateExp,
                    primeP, primeQ, expP, expQ, crtCoeff);

            KeyFactory factory = KeyFactory.getInstance("RSA");
            return factory.generatePrivate(keySpec);
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    private PublicKey getPublicKey(String publicKeyBase64) throws Exception {
        String pem = publicKeyBase64
                .replaceAll("\\-*BEGIN.*KEY\\-*", "")
                .replaceAll("\\-*END.*KEY\\-*", "");
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(pem));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(pubKeySpec);
        return publicKey;
    }

    @Override
    public String createJwt_RS256(Map<String, Object> credentialMap) {
        String privateKey = getPrivateKeyString();
        JwtBuilder builder = Jwts.builder()
                .setHeaderParam("alg", "RS256")
                .setHeaderParam("typ", "JWT");
        if (credentialMap != null && !credentialMap.isEmpty()) {
            for (Map.Entry<String, Object> entry : credentialMap.entrySet()) {
                if (entry.getValue() != null && !"".equals(entry.getValue())) {
                    builder.claim(entry.getKey(), entry.getValue());
                }
            }
        }

        builder.signWith(SignatureAlgorithm.RS256, getPrivateKey(privateKey));
        return builder.compact();
    }

    @Override
    public String createJwt_HS256(String secret, Boolean isSecretBase64, Map<String, Object> credentialMap) {
        byte[] apiKeySecretBytes;
        if (isSecretBase64) {
            apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
        } else {
            apiKeySecretBytes = secret.getBytes();
        }
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
        JwtBuilder builder = Jwts.builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT");

        if (credentialMap != null && !credentialMap.isEmpty()) {
            for (Map.Entry<String, Object> entry : credentialMap.entrySet()) {
                if (entry.getValue() != null && !"".equals(entry.getValue())) {
                    builder.claim(entry.getKey(), entry.getValue());
                }
            }
        }

        builder.signWith(SignatureAlgorithm.HS256, signingKey);
        return builder.compact();
    }

    @Override
    public Claims parseJwt_RS256(String jsonWebToken) {
        String publicKey = getPublicKeyString();
        try {
            Claims claims = Jwts.parser()
                   .setSigningKey(getPublicKey(publicKey))
                   .parseClaimsJws(jsonWebToken).getBody();
            return claims;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}  