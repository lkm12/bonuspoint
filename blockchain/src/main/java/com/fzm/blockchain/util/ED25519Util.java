package com.fzm.blockchain.util;

import com.fzm.blockchain.algorithm.eddsa.EdDSAEngine;
import com.fzm.blockchain.algorithm.eddsa.spec.EdDSANamedCurveTable;
import com.fzm.blockchain.algorithm.eddsa.spec.EdDSAParameterSpec;
import com.fzm.blockchain.algorithm.eddsa.spec.EdDSAPrivateKeySpec;
import com.fzm.blockchain.algorithm.eddsa.EdDSAPrivateKey;
import com.google.protobuf.ByteString;

import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Signature;

/**
 * 签名
 *
 * @author wangtao
 * @create 2018/6/15
 */

public class ED25519Util {

    private ED25519Util() {
        throw new AssertionError("不能实例化 ED25519Util");
    }

    /**
     * ed25519签名
     * @param requestByte
     * @param bytePrivateKey
     * @return
     * @throws Exception
     */
    public static ByteString sign(byte[] requestByte, byte[] bytePrivateKey) throws Exception{
        EdDSAParameterSpec spec = EdDSANamedCurveTable.getByName(EdDSANamedCurveTable.ED_25519);
        Signature sgr = new EdDSAEngine(MessageDigest.getInstance(spec.getHashAlgorithm()));

        EdDSAPrivateKeySpec privKey = new EdDSAPrivateKeySpec(bytePrivateKey, spec);
        PrivateKey sKey = new EdDSAPrivateKey(privKey);
        sgr.initSign(sKey);

        sgr.update(requestByte);
        byte[] sign = sgr.sign();
        return ByteString.copyFrom(sign, 0, 64);
    }
}
