# E-Commerce Clean Architecture

## Uygulama
```
- İlk ekranda uygulama açılırken önce animasyon görünüyor success veya error döndüğünde ise animasyon gizleniyor.

- Ardından içeride butonlar vasıtasıyla sepete eklenme yapıldığında otomatik olarak 
sepet sayfasına yönlendiriliyor ve sepet güncelleniyor SP'ye kayıt ediliyor.

- Sepet ekranında + veya - butonları ile adet güncelleme yapılıyor aynı zamanda silme butonu ile 
ürünü direkt sepetten çıkarabiliriz ve anlık olarak SP'ye kayıt ediliyor.

- Sağ alt köşedeki buy butonuna tıklayınca da direkt ürünler satın alınıyor 
eğer id'si 3 olan ürün var ise sepette önce bu ürün bulunmamaktadır diye 
uyarı veriliyor ve kullanıcı olsun devam et derse kalan ürünleri satın alıyor
SP temizleniyor ve önceki ekrana döndürülüyor.
```

## Test
```
- Shared Preferences ve Retrofit testi yazıldı

- Sharedpreferences package'ının altında SP testi bulunmakta bu testte data 
ekleniyor ve eklenen data çekilip aynı data mı diye kontrol ediliyor.

- Retrofit testinde ise aslında neredeyse aynı diyebiliriz sadece 
coroutine ile beraber mockwebserver üzerinden sorgu atıyoruz ve elimizdeki 
mock success json dosyası ile kıyas yapıyoruz eğer aynı ise testimiz geçiyor.
```

## Libraries

* [Coroutines](https://developer.android.com/kotlin/coroutines)
* [Navigation Architecture](https://developer.android.com/guide/navigation/navigation-getting-started)
* [Data Bindings](https://developer.android.com/topic/libraries/data-binding)
* [View Binding](https://developer.android.com/topic/libraries/data-binding)
* [Live Data](https://developer.android.com/topic/libraries/architecture/livedata)
* [Retrofit](https://square.github.io/retrofit/)
* [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
* [Glide](https://github.com/bumptech/glide)
* [Gson](https://github.com/google/gson)
