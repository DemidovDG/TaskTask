# AppSearchRepository
Приложение с динамическим поиском репозиториев сервиса https://github.com/, используя API (https://docs.github.com/en/rest). Приложение состоит из двух экранов.
> Рабочая версия на ветке **withoutgitlib**.
### Первый экран - экран поиска.
При вводе первых трёх символов подгружается результат поиска и отображается список, содержащий первые 10 результатов. В каждом элементе списка присутствует аватар владельца репозитория, полное название репозитория и его описание.
<p align="center">
  <img src="https://github.com/DemidovDG/pictures/raw/main/taskTask/search_1.png" width="270" height="540px"/>
  <img src="https://github.com/DemidovDG/pictures/raw/main/taskTask/found.png" width="270" height="540px"/></p>

При прокрутке в конец списка поиска прогружаются следующие 10 результатов.
<p align="center">
  <img src="https://github.com/DemidovDG/pictures/raw/main/taskTask/found_2.png" width="270" height="540px"/></p>

### Экран результатов
При нажатии на репозиторий пользователь попадает на экран деталей репозитория. На нём выводится информация о выбранном репозитории, присутствуют те же элементы, что и в списке на экране поиска и первые 30 issues репозитория.
<p align="center">
  <img src="https://github.com/DemidovDG/pictures/raw/main/taskTask/test_open_1.png" width="270" height="540px"/>
  <img src="https://github.com/DemidovDG/pictures/raw/main/taskTask/test_open_2.png" width="270" height="540px"/></p>
Иногда вместо issues может быть написано "Empty!", так как привышен лимит запросов:
>W/System.err: org.json.JSONException: Value {"message":"API rate limit exceeded for --.--.---.---. (But here's the good news: Authenticated requests get a higher rate limit. Check out the documentation for more details.)","documentation_url":"https:\/\/docs.github.com\/rest\/overview\/resources-in-the-rest-api#rate-limiting"} of type org.json.JSONObject cannot be converted to JSONArray
