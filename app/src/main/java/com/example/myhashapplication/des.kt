import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object DESUtil {

    private const val ALGORITHM = "DES"

    fun encrypt(plainText: String, key: String): String {
        val secretKey = SecretKeySpec(key.toByteArray(), ALGORITHM)
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val encryptedBytes = cipher.doFinal(plainText.toByteArray())
        return bytesToHex(encryptedBytes)
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val result = StringBuilder()
        for (b in bytes) {
            result.append(String.format("%02x", b))
        }
        return result.toString()
    }
}
