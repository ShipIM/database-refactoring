# database-refactoring

# Рефакторинг баз данных и приложений
## Сценарий 2: рефакторинг уже существующего проекта  
### Проект: курсовая работа по ИСБД  
### Студенты:  
- Павлов Александр
- Шипунов Илья

### Краткое описание проекта:  
#### Проект: Auction House (AH) в World of Warcraft (WoW)

**Описание:**

World of Warcraft (WoW) — это популярная многопользовательская онлайн-игра (MMORPG), разработанная Blizzard Entertainment. В игре присутствует внутриигровая торговая площадка, называемая **Auction House (AH)**, которая позволяет игрокам покупать и продавать различные предметы, используя внутриигровую валюту — **золото**.

**Основные возможности AH:**
- **Покупка предметов**: игроки могут искать и приобретать предметы, необходимые для развития персонажей (снаряжение, материалы для крафта, ресурсы и т.д.).
- **Продажа предметов**: игроки могут выставлять на продажу любые имеющиеся предметы, чтобы заработать золото.
- **Аукционные торги**: товары могут выставляться на торги, где другие игроки делают ставки, либо по фиксированной цене через систему "Купить сейчас".

**Цель проекта:**
Предоставить игрокам удобный механизм торговли, позволяющий эффективно обменивать предметы и зарабатывать внутриигровую валюту, стимулируя экономическое взаимодействие внутри игрового мира.

## Этапы рефакторинга
### Первый этап:
1. Написание swagger
2. Добавление описания архитектуры решения
3. Написание javadoc

### Второй этап:
1. Добавление метрик для существующих запросов
2. Подключение grafana и prometheus
3. Использование docker и docker-compose

### Третий этап:
1. Добавление unit-тестов
2. Добавление slf4j logger
3. Добавление кеширования
