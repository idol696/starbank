# **Банк «Star»**

# _Содержание проекта_

Данный проект направлен на разработку рекомендательной системы, способствующей оптимизации маркетинговых стратегий банка «Стар» и предоставляющей клиентам персонализированные банковские продукты. Основной целью является создание минимально жизнеспособного продукта (MVP), который будет осуществлять рекомендации по новым услугам и продуктам, основываясь на анализе предпочтений и поведения пользователей.

# _Технологический стек:_

1. _**Spring**_ — мощный фреймворк для разработки приложений на языке Java, который предоставляет обширные возможности для создания корпоративных приложений.


2. _**Swagger**_ — инструмент для документирования API, который предоставляет интерфейс для визуализации и тестирования RESTful веб-сервисов.


3. _**Spring Data JPA**_ — часть экосистемы Spring, которая упрощает работу с базами данных, предоставляя абстракцию над JPA (Java Persistence API), позволяет эффективно управлять доступом к данным, используя репозитории и упрощая выполнение операций CRUD (создание, чтение, обновление, удаление).


4. _**PostgreSQL**_ — объектно-реляционная система управления базами данных с открытым исходным кодом, известная своей надежностью и высокой производительностью.


5. _**H2**_ — легковесная реляционная база данных, написанная на Java, которая может работать как в памяти, так и в режиме хранения на диске.


6. _**Liquibase**_ — инструмент для управления версиями схемы базы данных, который позволяет отслеживать изменения в структуре базы данных с помощью скриптов миграции.


7. _**Caffeine Cache**_ — высокопроизводительный кэш для Java-приложений, который обеспечивает быструю доступность часто запрашиваемых данных. Caffeine использует алгоритмы кэширования, которые оптимизируют использование памяти и повышают скорость обработки запросов.





# _Архитектура приложения_

Вся архитектура проекта, техническое задание, а так же диаграммы классов и активности, можно посмотреть на [Wiki](https://github.com/idol696/starbank/wiki).


# _Основные функции_

* **Персонализированные рекомендации:** Система осуществляет анализ клиентских данных с целью генерации индивидуализированных рекомендаций по банковским продуктам, соответствующим специфическим потребностям и предпочтениям пользователей.
* **Динамическое управление правилами рекомендаций:** Система предоставляет возможность внедрения и модификации правил отображения рекомендаций без необходимости в перекомпиляции и перезапуске приложения, что обеспечивает высокую гибкость и оперативность в адаптации к изменяющимся условиям и требованиям рынка.

# _User Stories_

`- Как пользователь банка «Стар», я хочу получать рекомендации по банковским продуктам на основе моих транзакций, чтобы я мог выбирать наиболее подходящие предложения.`

`- Как маркетолог, я хочу анализировать статистику рекомендаций, чтобы понимать, какие продукты лучше работают.`

`- Как аналитик, я хочу редактировать правила рекомендаций, чтобы быстро адаптировать систему к новым бизнес-требованиям.`

# _Функциональные возможности_

1. Система предоставляет пользователю список рекомендованных банковских продуктов.
2. Система использует набор правил для генерации рекомендаций на основе данных пользователя.
3. Система обслуживает TelegramBot позволяющий получать рекомендации. 
4. Система имеет REST API для получения рекомендаций по UUID пользователя.
5. Система предоставляет возможность редактирования правил, а именно:

    * содержит правило, по которому можно выполнять условие для показа рекомендации
    * содержит список правил показа для пользователя, также содержит текст рекомендации и название предложения
    * позволяет редактировать правила, сохраняя в базе данных

# Установка

### 1. Подготовка окружения:

   Прежде всего, убедитесь, что у вас установлен и правильно настроен сервер PostgreSQL. Это важный шаг, так как Starbank использует данную СУБД для хранения и управления данными. 

### 2. Создание базы данных:

   После успешной установки PostgreSQL вам необходимо создать новую базу данных с именем starbank. Это можно сделать с помощью командной строки или графического интерфейса pgAdmin. В командной строке выполните следующую команду:

   `CREATE DATABASE starbank;`

Убедитесь, что база данных создана успешно, так как она станет основой для работы приложения.

### 3. Запуск приложения  

   Для этого откройте терминал и выполните следующую команду:

   `java -jar -Dpostgres-login=логин -Dpostgres-passwd=пароль -Dpostgres-login=логин_сервера -Dtelegram-botname=имя_бота -Dtelegram-token=токен_бота starbank.jar`

   Обратите внимание, что вам нужно заменить логин и пароль на ваши собственные учетные данные для доступа к базе данных PostgresSQL. Также не забудьте указать имя вашего Telegram-бота и его токен, если вы планируете интеграцию с Telegram.

#### Дополнительные возможности

Вы можете указать строку подключение к вашей H2 банковской системе, вписав дополнительный параметр -Dh2.datasource.url=строка подключения к БД (пример: -Dh2.datasource.url=jdbc:h2:file:./transaction;DB_CLOSE_DELAY=-1;ACCESS_MODE_DATA=r)

Также вы можете указать свою собственную строку подключения к Postgres SQL. Если хотите использовать другие параметры подключения впишите в параметр -Dpostgres-url=строка подключения к БД (пример -Dpostgres-url=jdbc:postgresql://localhost:5432/starbank)

_Строки в примерах также является строками по умолчанию, с проектом поставляется демонстрационная база, в корне проекта, она будет подключена "по умолчанию"._

### 4. Доступ к системе  

   После успешного запуска приложения, вы сможете получить доступ к системе Starbank. Откройте веб-браузер и введите следующий адрес:

   `https://localhost:8080`




# _Разработчики_

1. [Роман Кузьмин](https://github.com/idol696)
2. [Алина Черемискина](https://github.com/linskay)
3. [Мария Голдовская](https://github.com/goldovskaya-m)
4. [Варвара Калинина](https://github.com/varyansan)


![img.png](img.png)
`пы.сы: примерное фото команды мечты, а это и есть МЫ!`


