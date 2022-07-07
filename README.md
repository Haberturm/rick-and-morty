# rickAndMorty

## Архитектура
Приложение построено по принципу Clean Architecture и в соответсвии сним разделено на три слоя: 
- domain
- data
- presentation
#### Domain cлой 
Содержит абстрактное представление репозитория ( *interface Repository* ), доменные модели данных и общие обертки для ошибок ( *sealed class AppException* ) и состояний ( *sealed class ApiState* ). Этот слой полностью незавсим от Android SDK.
#### Data cлой 
Содержит реализацию абстрактных представлений доменного слоя ( *class RepositoryImpl* ). Здесь находиться класса связанные с базой данных, её DAO и структуры связнные с сетевым API. Также модели данных API и бд и их мапперы. 
#### Presentation cлой 
Постороен в соответсвии с MVVM. Содержит фрагменты, ViewModelи, адаптеры и разную логику отображения. А также модели данных для отображения и их мапперы. 

## Связь с сетью
*Метонахождение: data -> api*
Для работы с сетью используется библиотека **Retrofit**.  Инстанс ее находиться в *object RetrofitClient* ~~На этапе тестирования пришло понимание, почему хранить в object это плохо~~. Парсинг json осуществляет с помощью библиотеки **GSON**, интерсепторы для логирования и прочего реализованы с помощью **okHttp**. Интрефейс для работы с сетью реализован в* interface RickAndMortyApi *

## База данных
*Метонахождение: data -> db*
Для работы с SQLite используется библиотека **ROOM**. Инстанс бд находиться в *abstract class RickAndMortyDatabase*
который наследует *RoomDatabase*. С помощью анотации @ TypeConverters добален конвертер ( реализация в классе Converters, метод fromArrayList() конвертирует ArrayList в строку, fromString() конвертирует строку в ArrayList ). В классе *RickAndMortyDatabase * находяться абстрактные функции котрые возвращаю dao каждой сущности и companion object в котором находиться инстрансе бд в виде снглтона. **База данных является своего рода single source of truth, то есть например, прежде чем данные из сети попадут к пользователю они сначала закешируются в бд и уже оттуда пойдут к пользователю. **

## Dependency injection
*Метонахождение: di *
Для недрения зависимотей используется библиотека** Dagger2**.  Генерация графа завсимотей происходит *class RickAndMortyApp*, который является наследников *DaggerApplication() *, с помощью билдера *AppComponent*
*interface AppComponent* наследуется от AndroidInjector, котрый позволяет инжектить зависимости в базовые андроид классы, содержит в себе AppModule, который в свою очередь содержит в себе оснвные модули (у меня это *DataModule, ViewModelModule, ActivityBindingModule* ) и байндит application context
*DataModule* байндит репозиторий и возвращает его реализацию RepositoryImpl. Содержит в себе *DataBaseModule* , котрый провайдит бд и CoroutineDispatcherModule, который провайдит io context для корутин.
*ViewModelModule*  байндит generic фабрику для вью моделей и методы для байнда конкретной вью модели, которые хранят зависмости в мапе с помощью анотации @ IntoMap и содержит кастомную анотацию для @ ViewModelKey, где ключем явлется конкретный класс ViewModel
*FragmentBindingModule * содержит байнды для фрагментов, которые помечены специальным скопом @ FragmnetScoped

### Далее речь будет идти о сущности *character*, для остальных сущностей всё работает аналогично
## Реализация репозитория
*class RepositoryImpl* в его конструктор инжектитятся *database: RickAndMortyDatabase* - база данных и *ioDispatcher: CoroutineDispatcher = Dispatchers.IO* - io context для тестирования. 

Метод `fun updateCharacters(page: Int): Flow<ApiState<Unit>>`  - обновляет данные по персонажам в бд и эмитит ApiState (Success или Error ). Обертка в этот класc просиходит в методе ***updateState()*** в пареметры которому передаётся `remoteDataSource: suspend () -> Response<D>,` - метод из интрефеса для связи с сетью, `insertDataInDB: (T1) -> Unit` - метод из dao для того чтобы положить данные в бд и `insertInfoDataInDB: ((T2) -> Unit)? = null,` - метод из info dao чтобы положить информацию о данных в бд, имеет дефолтное значение, так как в некотрых случаях он не нужен. Метод возвращает АpiState (Success или Error ) в зависимости от успеха операции получения данных из сети и помещения их в бд.

Метод `fun getCharacters(page: Int): Flow<ApiState<Characters>>` - получает данные из бд и эмитит ApiState ( Success(data domain) или Error ) .Обертка в этот класc просиходит в методе ***dataState()* ** в параметры которому передаются  `mapper: DataMapper` (маппер для мапинга из data сущностей в domain сущности, реализован в виде абстрактного класса *DataMapper *, где у каждой сущности своя реализация),  `localDataSource: () -> R1` - метод из dao для доступа к данным из бд, `noinline localDataInfoSource: (() -> R2)? = null` метод из info dao для доступа к информации о данных из бд. Метод возвращает АpiState (Success(domain data) или Error ) в зависимости от успеха операции получения данных из бд

Метод `fun getFilteredCharacters(name: String, status: String, species: String, type: String, gender: String): Flow<ApiState<Characters>> ` емитит ApiState ( Success(data domain) или Error ) с помощью уже известного ***dataState()* ** куда передаётся метод из dao со специальным query фильтром

Метод `fun updateSingleCharacter(id: Int): Flow<ApiState<Unit>>` обновляет или добавлет одну строку в таблице персонажей с соотвествуюшим id

Метод `fun getSingleCharacter(id: Int): Flow<ApiState<CharacterResults>>`  возвращает персонажа из бд с соответвующим id или ошибку ( если персонажа, например не существует )

Метод `fun updateCharactersByIdList(ids: List<Int>): Flow<ApiState<Unit>>` обновляет или добавлет строкчки в таблице персонажей с соотвествуюшими id 

Метод `fun getCharactersByIdList(ids: List<Int>): Flow<ApiState<Characters>>`  возвращает персонажей из бд с соответвующими id или ошибку ( если персонажа, например не существует )

## Отображение списка данных
*Метонахождение: presentation -> screens*
Логика отображения данных находиться в паре Fragment + ViewModel. 
*CharactersMainFragment* отображает список персонажей. Класс наследуется от *DaggerFragment* для упрощенного внедрения зависимотей. Например, фабрики вью моделей *viewModelFactory: ViewModelFactory*.
В *onCreate* происходит инициализация класса *Navigation* который я использую для навигации и инициализация адаптера для recyclerView, в данном случае *CharacterListAdapter*, в конструктор которого переадётся контекст и реализация интрефейса* CharacterListAdapter.ActionClickListener* для обработки клика на элемент.
В onCreateView инициализируется view binding для доступа к элементам разметки.

В соответвуещей ViewModel - *CharactersMainViewModel* для фрагмента существует поля     
```
private val _uiState = MutableLiveData<UiState<List<CharacterUi>>>(null)
val uiState: LiveData<UiState<List<CharacterUi>>>
        get() = _uiState
```
Типа *LiveData< UiState >*, где UiState - sealed class состояний - UiState.Data(data), UiState.Loading, UiState.Error(error). Это поле обсервиться из фрагмента и в зависмости от сотстояний отображает список данных, индикатор загрузки или ошибку. 
в блоке* init{}* запускается метод *getData()*, который запускает метод *repository.updateCharacters(page)* ( repository: Repository - инжектиться в конструткор вью модели ) который возвращает состояние запроса на обновление данных, внезавсимости от состояния, который вернул этот метод, после получения состояния вызовется метод  *repository.getCharacters(page)*  в зависомости от состояния кторый вернёт этот метод, данные либо мапяться в ui сущность и отображаются либо отображается ошибка. 
Схематично метод выглядит так: 
```
private fun getData() {
        val page = currentPage.value!!
        *отображение загрузки *
        viewModelScope.launch {
            repository.updateCharacters(page)
                .onEach { networkRequestState ->
                    * обработка возможной ошибки сети *
                    }
                    repository.getCharacters(page)
                        .onEach { data ->
                            when (data) {
                                is ApiState.Success<Characters> -> {
                                    * отображение данный *
                                }
                                is ApiState.Error -> {
                                   * отображение ошибки * 
                            }
                        }.launchIn(this)
                }
                .launchIn(this)
        }
    }
```
Метод для филтрации данных `private fun getFilteredData()` работает по похожему принципу. 
Стоит упомянуть, что фрагмент для филтрации и фрагмент с данными имеет общую ViewModel это упрощает процесс работы, так как избавляет от проброса данных между вью моделями. 

Смена страниц происходит при помощи изменения значения поля *currentPage* и повторного вызова метода *getData* с новым значением страницы.

## Отображение детальной информации 
Переход на страницу с деталями происходит благодоря вызову метода Navigation().replaceFragmnet(...) в методе showDetail(id) в адаптере recycler view. ID нужного элемента передаётся при навигации на новый фрагмент в качестве аргумента внутри бандла. 
В новом фрагменте *CharacterDetailFragment*, который тоже наследуется от DaggerFragment, при получении аргумента происходит вызов метода getData(argument) из ViewModel. Это метод аналогичен getData() из прошлой главы за исключением, что тут вызваются методы `repository.updateSingleCharacter(id)` и `repository.getSingleCharacter(id)`, речь о котрых шла выше. 
В случае успешного получения данных вызывается метод `private fun getEpisodes(ids: List<Int>)`, в котором по тому же принципу вызваются методы репозитория `repository.updateEpisodesByIdList(ids)` и `repository.getEpisodesByIdList(ids)`. *ids * - с id эпизодов, который мы получаем при маппинге данных в *CharacterDetailUi*. 

## Тестирование
Для тестирования используются библиотеки JUnit4 + Mockk.
Для начала проведм тестирование мапперов. 
```
    @Test
    fun `Mapping Episode to EpisodeUi is correct`() {
        assertEquals(listEpisodeUi, EpisodesUiMapper().fromDomainToUi(episodeDomain))
    }
```
Симулируя разного рода случаи можно убедиться, что мапперы отрабатывают корректно. 

Далее протестируем репозиторий. Для начала в методе:
```
@Before
    fun initRepository() {
        initDB()
        repository = RepositoryImpl(db, testDispatcher)
    }
```

```
private fun initDB() {
			...
		every { db.episodesDao().getEpisodeById(episodeIdToTest) } returns singleEpisode
        every { db.episodesDao().getEpisodeById(episodeErrorId) } returns null
}
```
Мы замокаем методы репозитория. 
А в методе `fun 'Get single episode()'` мы убедимся, что соотвветвующий метод репозитория отработает, как предпологалось.
```
@Test
    fun `Get single episode`() = runBlockingTest { 
			...
		repository.getSingleEpisode(episodeIdToTest)
            	.onEach {
                	assertEquals( ApiState.Success(episode), it )
           	 }
           	 .launchIn(this)
}
```






















