# codeforensics

Здесь 5 модулей (пока):

transfer - суда складываем все дата-классы для обмена с внешними системами (должен зависеть только от внешних библиотек)
model - здесь все что касается сохранения данных (работа с бд, сущности, репозитории) (зависит от внешних и от transfer)

upload-app, parser-app, rest-app - непосредственно сами исполняемые приложения (зависит от внешних и от transfer и/или model)

вообще можно было бы воткнуть модуль services между model и app, но пока бизнес логики почти нет он не нужен
