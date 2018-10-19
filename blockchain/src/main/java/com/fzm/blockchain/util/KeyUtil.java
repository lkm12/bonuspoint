package com.fzm.blockchain.util;

import com.fzm.blockchain.algorithm.eddsa.spec.EdDSANamedCurveTable;
import com.fzm.blockchain.algorithm.eddsa.spec.EdDSAParameterSpec;
import com.fzm.blockchain.algorithm.eddsa.spec.EdDSAPrivateKeySpec;
import com.fzm.blockchain.algorithm.sha3.sha.Keccak;
import com.fzm.blockchain.algorithm.sha3.sha.Parameters;
import com.fzm.blockchain.algorithm.eddsa.EdDSAPrivateKey;

/**
 * 公私钥工具
 *
 * @author wangtao
 * @create 2018/6/15
 */

public class KeyUtil {

    private KeyUtil() {
        throw new AssertionError("不能实例化 KeyUtil");
    }

    /**
     * 获取私钥
     * @param password
     * @param random
     * @return
     */
    public static String privateKey(String password, String random) {
        String message = password + random;
        Keccak keccak = new Keccak();
        String hexKey = HexUtil.bytesToHex(message.getBytes());
        byte[] hash = keccak.getHash(hexKey.getBytes(), Parameters.KECCAK_256);
        return HexUtil.bytesToHex(hash);
    }

    /**
     * 获取公钥
     * @param privateKey
     * @return
     */
    public static String publicKey(String privateKey) {
        EdDSAParameterSpec spec = EdDSANamedCurveTable.getByName(EdDSANamedCurveTable.ED_25519);
        EdDSAPrivateKeySpec privKey = new EdDSAPrivateKeySpec(HexUtil.hexToBytes(privateKey), spec);
        EdDSAPrivateKey sKey = new EdDSAPrivateKey(privKey);
        return HexUtil.bytesToHex(sKey.getAbyte());
    }


}
