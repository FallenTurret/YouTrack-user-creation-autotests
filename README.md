# YouTrack user creation autotests

## Требования

1. Нужен пользователь root с паролем root
2. Должен быть установлен WebDriver для одного из браузеров(по умолчанию используется Chrome), путь к нему должен быть указан в `PATH`

## Запуск

````
$ gradle build
````
## Настройки

Поменять браузер и базовый URL можно [здесь](src/main/java/ru/spb/hse/youtrack/Settings.java)
