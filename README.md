# **Банк «Star»**

# _Содержание проекта_

Проект направлен на разработку рекомендательной системы, которая поможет банку «Стар» улучшить маркетинг и предложить клиентам персонализированные банковские продукты. Целью является создание минимального жизнеспособного продукта (MVP), который будет предоставлять рекомендации по новым услугам и продуктам.

Стек технологиий: Spring, Swagger, JPA, JDBC, PostgreSQL

# _Архитектура приложения_

Вся архитектура проекта, а так же диаграммы классов и активности, можно посмотреть на [Wiki](https://github.com/idol696/starbank/wiki).

# _Основные функции_

* Персонализированные рекомендации: Система анализирует данные клиентов и предлагать им подходящие банковские продукты.
* Позволяет внедрять и редактировать правила показа рекомендаций без перекомпиляции и перезапуска приложения.

# _User Stories_

`- Как пользователь банка «Стар», я хочу получать рекомендации по банковским продуктам на основе моих транзакций, чтобы я мог выбирать наиболее подходящие предложения.`

# _Functional Requirements_

1. Система предоставляет пользователю список рекомендованных банковских продуктов.
2. Система использует набор правил для генерации рекомендаций на основе данных пользователя.
3. Система имеет REST API для получения рекомендаций по UUID пользователя.
4. Система предоставляет возможность редактирования правил, а именно:
    * содержит правило, по которому можно выполнять условие для показа рекомендации
    * содержит список правил показа для пользователя, также содержит текст рекомендации и название предложения
    * позволяет редактировать правила, сохраняя в базе данных

# Установка

1. Убедитесь что у вас установлен сервер PostgresSQL
2. Создайте базу данных starbank
3. Запустите java -jar -Dpostgres-login=логин -Dpostgres-passwd=пароль starbank.jar
4. После этого система станет доступна (локально: https://localhost:8080)

# _Разработчики_

1. [Роман Кузьмин](https://github.com/idol696)
2. [Алина Черемискина](https://github.com/linskay)
3. [Мария Голдовская](https://github.com/goldovskaya-m)
4. [Варвара Калинина](https://github.com/varyansan)

`пы.сы: тут должно быть общее фото`