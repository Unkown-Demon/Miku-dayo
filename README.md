# Miku Dayo - Prank App Project

Bu loyiha sizning soʻrovingiz boʻyicha Kotlin tilida Android uchun yaratilgan "prank" ilovadir.

## Loyihani Ishga Tushirish Boʻyicha Koʻrsatmalar

1.  **Loyihani Ochish:** Android Studio'da `MikuDayoApp` papkasini oching.
2.  **Resurslarni Joylashtirish:** Ilova toʻgʻri ishlashi uchun quyidagi fayllarni tegishli joylarga joylashtiring:

    ### A. Media Fayllar (Video va Audio)
    *   **Joylashuv:** `MikuDayoApp/app/src/main/res/raw/`
    *   **Fayllar:**
        *   `start.mp4` (Boshlanish videosi)
        *   `audio1.mp3` (Qizil tugma uchun audio)
        *   `audio2.mp3` (Koʻk tugma uchun audio)
    *   **Hozirda bu yerda placeholder fayllar mavjud. Ularni oʻzingizning haqiqiy fayllaringiz bilan almashtiring.**

    ### B. Rasmlar (Logo va Ilova Ikonkasi)
    *   **1. Suzib Yuruvchi Rasm (Bouncing Logo):**
        *   **Fayl:** Siz yuborgan birinchi rasm.
        *   **Joylashuv:** `MikuDayoApp/app/src/main/res/drawable/` papkasiga joylashtiring va nomini **`prank_logo.png`** yoki **`prank_logo.jpg`** deb oʻzgartiring.
        *   **Eslatma:** Hozirda `MainActivity.kt` faylida `R.drawable.prank_logo` ishlatilgan. Agar siz PNG formatida joylashtirsangiz, fayl nomini shunday qoldiring.

    *   **2. Ilova Ikonkasi (App Icon):**
        *   **Fayl:** Siz yuborgan ikkinchi rasm.
        *   **Joylashuv:** Android Studio'da **`mipmap`** resurslarini yangilash orqali ikonkani oʻrnating. Odatda, bu **`app/src/main/res/mipmap-xxxhdpi/`** kabi papkalarda joylashadi.

3.  **Build va Oʻrnatish:** Loyihani build qiling va qurilmangizga oʻrnating.

## Ilova Logikasi

*   **Ruxsatlar:** Ilova boshida chalgʻitish uchun fayl va audio ruxsatlarini soʻraydi.
*   **Video:** `start.mp4` avtomatik ravishda toʻliq ekranda ijro etiladi.
*   **Logo Ekrani:** Video tugagach, oq fonda "Meni boss" yozuvi va suzib yuruvchi rasm paydo boʻladi.
*   **Audio Tanlash:** Suzib yuruvchi rasm bosilganda, qora fonda qizil va koʻk tugmalar paydo boʻladi.
*   **Prank Effekti:** Audio tanlangandan soʻng, qurilmaning ovoz balandligi maksimal darajaga koʻtariladi va doimiy ravishda shu holatda ushlab turiladi. Audio loop qilinadi.
*   **Avtomatik Yopilish:** Audio tanlangandan 5 daqiqa oʻtgach, ilova avtomatik ravishda yopiladi (`finishAffinity()` orqali).

## Fayllar Roʻyxati

Quyidagi fayllar sizga zip arxivi ichida yuboriladi:

*   `MikuDayoApp/` (Asosiy loyiha papkasi)
    *   `app/`
        *   `src/main/java/com/example/mikudayo/MainActivity.kt`
        *   `src/main/java/com.example.mikudayo/AudioSelectActivity.kt`
        *   `src/main/res/layout/activity_main.xml`
        *   `src/main/res/layout/activity_audio_select.xml`
        *   `src/main/res/drawable/circle_red.xml`
        *   `src/main/res/drawable/circle_blue.xml`
        *   `src/main/res/raw/start.mp4` (Placeholder)
        *   `src/main/res/raw/audio1.mp3` (Placeholder)
        *   `src/main/res/raw/audio2.mp3` (Placeholder)
        *   `src/main/AndroidManifest.xml`
        *   ... va boshqa Gradle konfiguratsiya fayllari.
    *   `bouncing_logo.jpg` (Sizning suzib yuruvchi rasmingiz)
    *   `app_icon.jpg` (Sizning ilova ikonangiz)
