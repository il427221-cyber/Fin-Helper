package ru.myproject.finhelper.ui.screenshot

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

@SuppressLint("Recycle")
//Функция для сохранения картинки в галерею
fun saveImageToGallery(context: Context, bitmap: Bitmap, filename: String): Boolean {
    var uri: Uri? = null
    var outputStream: OutputStream? = null
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                //Имя файла, которое видно пользователю
                put(MediaStore.MediaColumns.DISPLAY_NAME,filename)
                //Тип файла
                put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                //Путь сохранения - Картинки/Изображения -> Подпапка "VATCalculator"
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "VATCalculator")
            }
            val contentResolver = context.contentResolver
            //мы создаем uri в системе, по которому можно записать данные
            uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            /*Мы открываем поток записи (OutputStream) по полученному Uri.
             Через этот поток мы будем “заливать” данные Bitmap.
             */
            outputStream = uri?.let { contentResolver.openOutputStream(it) }
        } else {
            /*
            Для старых версий Android используется традиционный подход с прямым доступом к файловой системе,
            требующий разрешения WRITE_EXTERNAL_STORAGE
             */

            //Получаем путь к публичной директории картинок.
            val imagesDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES + File.separator + "VATCalculator")
            //Если папки "VATCalculator" не существует, то создаем ее
            if (!imagesDir.exists()) {
                imagesDir.mkdirs()
            }
            //Создаём объект File для изображения
            val imageFile = File(imagesDir, "$filename.png")
            //Открываем обычный файловый поток для записи
            outputStream = FileOutputStream(imageFile)
            //Создаём Uri из объекта File для дальнейшего использования
            uri = Uri.fromFile(imageFile)
        }
        //Вне зависимости от версии Android, здесь происходит сжатие Bitmap в формат PNG и запись его в outputStream. use
        outputStream?.use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            it.flush()
        }
        // Уведомить медиа-сканер, что новый файл доступен
        if (uri != null) {
            val scanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            scanIntent.data = uri
            /*
            На старых версиях Android это нужно, чтобы система (и, соответственно, галерея) “увидела” новый файл.
            На новых версиях MediaStore делает это автоматически.
             */
            context.sendBroadcast(scanIntent)
        }
        return true
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    } finally {
        outputStream?.close()
    }
}
// Функция для отправки Bitmap
fun shareImage(context: Context, bitmap: Bitmap, filename: String) {
    /*
    Чтобы поделиться скриншотом, он должен быть доступен по Uri.
    Нельзя просто передать Bitmap. Поэтому мы временно сохраняем Bitmap в кэш приложения.
     */
    try {
        /*
        Создаём подпапку shared_images внутри директории кэша приложения.
        Это временное хранилище, которое система может очищать
         */
        val cachePath = File(context.externalCacheDir, "shared_images")
        cachePath.mkdirs()
        //Создаём временный файл
        val file = File(cachePath, filename)
        //Сохраняем Bitmap в этот временный файл
        val fileOutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        fileOutputStream.flush()
        fileOutputStream.close()
        /*
        FileProvider — это специальный компонент, который безопасно создаёт content:// Uri для файлов
        в указанных директориях
         */
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Поделиться скриншотом"))
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Не удалось поделиться скриншотом", Toast.LENGTH_SHORT).show()
    }
}