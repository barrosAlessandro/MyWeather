package br.com.wcabral.myweather.util

import android.content.Context
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import java.io.File

class Security(
    private val ctx: Context,
    private val value: String
) {
    private fun createdEncryptedFile(): EncryptedFile {
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val masterKeyAlias =
            MasterKeys.getOrCreate(keyGenParameterSpec)
        val file = File(ctx.filesDir, "myfile.txt")
        if (file.exists()) {
            file.delete()
        }

        return EncryptedFile.Builder(
            file,
            ctx,
            masterKeyAlias,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()
    }

    private fun saveFile() {
        createdEncryptedFile().openFileOutput().use { writer ->
            writer.write(value.toByteArray())
        }
    }

    fun readFile(): String {
        this.saveFile()
        var result = ""
        createdEncryptedFile().openFileInput().use { inputStream ->
            result = inputStream.readBytes().decodeToString()
        }
        return result
    }
}