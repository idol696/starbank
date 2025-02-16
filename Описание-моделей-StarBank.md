# Описание моделей StarBank

## 1. User  
**Файл:** `User.java`  
Представляет пользователя системы StarBank.

### Поля:
```java
private String id; // Уникальный идентификатор пользователя
private String name; // Имя пользователя
```

### Методы:
```java
public String getId(); // Возвращает ID пользователя
public String getName(); // Возвращает имя пользователя
```

---

## 2. Product  
**Файл:** `Product.java`  
Представляет банковский продукт, который может быть рекомендован пользователю.

### Поля:
```java
private String id; // Уникальный идентификатор продукта
private String name; // Название продукта
private String type; // Тип продукта (например, "DEBIT", "CREDIT", "INVEST")
private String description; // Описание продукта
```

### Методы:
```java
public String getId(); // Возвращает ID продукта
public String getName(); // Возвращает название продукта
public String getType(); // Возвращает тип продукта
public String getDescription(); // Возвращает описание продукта
```

---

## 3. Transaction  
**Файл:** `Transaction.java`  
Представляет транзакцию пользователя по продукту.

### Поля:
```java
private String id; // Уникальный идентификатор транзакции
private User user; // Пользователь, совершивший транзакцию
private Product product; // Продукт, связанный с транзакцией
private double amount; // Сумма транзакции
private String type; // Тип транзакции (DEPOSIT или WITHDRAW)
private Timestamp date; // Дата и время транзакции
```

### Методы:
```java
public String getId(); // Возвращает ID транзакции
public User getUser(); // Возвращает пользователя
public Product getProduct(); // Возвращает продукт
public double getAmount(); // Возвращает сумму транзакции
public String getType(); // Возвращает тип транзакции
public Timestamp getDate(); // Возвращает дату и время транзакции
```

---

## 4. Recommendation  
**Файл:** `Recommendation.java`  
Рекомендация, которую может получить пользователь.

### Поля:
```java
private String id; // Уникальный идентификатор рекомендации
private String name; // Название рекомендации
private String text; // Текстовое описание рекомендации
```

### Методы:
```java
public String getId(); // Возвращает ID рекомендации
public String getName(); // Возвращает название рекомендации
public String getText(); // Возвращает текст рекомендации
```

---

## 5. RecommendationResponse  
**Файл:** `RecommendationResponse.java`  
Отвечает за формат вывода рекомендаций для пользователя.

### Поля:
```java
private String userId; // Уникальный идентификатор пользователя
private List<Recommendation> recommendations; // Список рекомендаций
```

### Методы:
```java
public static RecommendationResponse empty(); // Возвращает пустой объект рекомендаций
public String getUserId(); // Возвращает ID пользователя
public void setUserId(String userId); // Устанавливает ID пользователя
public List<Recommendation> getRecommendations(); // Возвращает список рекомендаций
public void setRecommendations(List<Recommendation> recommendations); // Устанавливает список рекомендаций
```

---

## 6. Rule  
**Файл:** `Rule.java`  
Описывает правило, которое используется для формирования рекомендаций.

### Поля:
```java
private Long id; // Уникальный идентификатор правила
private RuleSet ruleSet; // Связанный набор правил
private String query; // Запрос, связанный с правилом
private String argumentsJson; // JSON-список аргументов
private boolean negate; // Флаг инверсии результата
```

### Методы:
```java
public String getQuery(); // Возвращает запрос правила
public void setQuery(String query); // Устанавливает запрос правила
public boolean isNegate(); // Возвращает флаг инверсии
public void setNegate(boolean negate); // Устанавливает флаг инверсии
public List<String> getArguments(); // Возвращает список аргументов правила
public void setArguments(List<String> arguments); // Устанавливает аргументы правила
public RuleSet getRuleSet(); // Возвращает набор правил
public void setRuleSet(RuleSet ruleSet); // Устанавливает набор правил
```

---

## 7. RuleSet  
**Файл:** `RuleSet.java`  
Группирует правила, относящиеся к одному продукту.

### Поля:
```java
private Long id; // Уникальный идентификатор набора правил
private UUID productId; // Уникальный идентификатор продукта
private String productName; // Название продукта
private String productText; // Описание продукта
private List<Rule> rules; // Список правил
```

### Методы:
```java
public UUID getProductId(); // Возвращает ID продукта
public void setProductId(UUID productId); // Устанавливает ID продукта
public String getProductName(); // Возвращает название продукта
public void setProductName(String productName); // Устанавливает название продукта
public String getProductText(); // Возвращает описание продукта
public void setProductText(String productText); // Устанавливает описание продукта
public List<Rule> getRules(); // Возвращает список правил
public void setRules(List<Rule> rules); // Устанавливает список правил
```

---

## 8. RuleSetWrapper  
**Файл:** `RuleSetWrapper.java`  
Обертка для списка наборов правил.

### Поля:
```java
private List<RuleSet> data; // Список наборов правил
```

### Методы:
```java
public List<RuleSet> getData(); // Возвращает список наборов правил
```

---

## 9. RuleStat  
**Файл:** `RuleStat.java`  
Статистика срабатывания правил.

### Поля:
```java
private Long id; // Уникальный идентификатор статистики
private Long ruleId; // ID правила
private int count; // Количество срабатываний
```

### Методы:
```java
public void increment(); // Увеличивает счетчик на 1
public Long getRuleId(); // Возвращает ID правила
public int getCount(); // Возвращает количество срабатываний
```

---

## 10. Связи между моделями
- **User** → совершает **Transaction**.  
- **Transaction** → относится к **Product**.  
- **Product** → может входить в **RuleSet**.  
- **RuleSet** → состоит из **Rule**.  
- **Rule** → используется для формирования **Recommendation**.  
- **Recommendation** → формируется на основе правил и отправляется пользователю.  

---

### *Примечание:*  
Этот раздел **Wiki GitHub** документирует ключевые модели проекта **StarBank** и их взаимосвязи. 
