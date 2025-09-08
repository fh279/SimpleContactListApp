Из MainActivity:

/** Немного истории по работе над ViewModel.
* Вот так это было по старому:
* private val viewModel = MyViewModel(stringResourcesProvider = StringResourcesProvider(this))
* - не будет работать в случае переворота экрана, потому что это кастомный конструктор и он
* инстанциируетсся при пересоздании активити и данные затираются.
* Затем был найден другой вариант:
* private val viewModel: MyViewModel by viewModels() Поворот экрана заработал, данные не перезатираются.
* Но ТАК это работает только в случае если во Вьюмодель не надо передавать аргументы.
* И тогда стал вопрос - как сделать так что б и аргументы во ViewModel можно было передавать,
* и она не умирала с пересозданием Activity. Тогда я узнал что можно
* использовать ViewModelProvider.Factory - провайдер, в который надо передать фабрику по созданию
* вьюмоделей. И тогда можно вне активити создать фабрику, сделать lateinit вьюмодель,
* а потом вьюмодель проинициализировать в активити. и вот эта конструкция будет хранить состояния не умирая при пересоздании активности.
*
* Задача со звёздочкой: написать свой делегат для получения кастомизируемых ViewModel. Когда-нибудь.
*/

enableEdgeToEdge() - вызывается в Scaffold, делает так что приложение отрисовывается даже на статус баре(или как он называется). Короче почти FullScreen.
imePadding() - вызывается на Modifier, работает в паре с enableEdgeToEdge(), позволяет скроллить интерфейс так что бы клавиатура не перекрывала активное поле ввода. 

val state: State<ListState> = viewModel.state.collectAsStateWithLifecycle()
val listState: ListState = state.value
// 1. Преобразует StateFlow (тип из корутин) в State (тип из compose)
// 2. Здесь происходит подписка на StateFlow. То есть мы можем следить за измеенениями нашего state.
// Вот такой момент с двумя переменными мне не нравится. Он экономит много раз обращение к value, но выглядит всратенько.
// val localWindowInsets: LocalWindowInsets = LocalWindowInsets()

// Items использован что бы задать composable скоуп и иметь возможность добавить изображение emptyState'а.
items(arrayListOf("")) {


// .weight(0f) - "убивает" скроллабельность всего головного контейнера.

Из ViewModel:

// Либо вернуть использование StringResourcesProvider (больше - в опытных целях, чем по необходимости, ибо можно обойти необходимость в этой штуке и использовать композный stringResource. Либо убрать и вернуть получение вьюмодели через by viewModels().
private val stringResourcesProvider: StringResourcesProvider

// Это - изменяемое состояние, которое хранится внутри ViewModel'и. Изменение этого состояния
// происходит из ВьюМодели.
private val _state: MutableStateFlow<ListState> = MutableStateFlow(ListState())
/** Есть 3 класса:
*  - StateFlow,
*  - SharedFlow,
*  - Flow
*  Если они должны быть изменяемыми, в начало имени добавляется Mutable.
*  Вроде (не точно) эти классы реализуют Observer.

   private val _state: MutableStateFlow<ListState> = MutableStateFlow(ListState())
   Это состояние, на которое мы будем подписываться внутри нашей Activity
   (а именно - нашей composable функции)
   val state: StateFlow<ListState> = _state.asStateFlow()

/** Сам метод (как и stringResourcesProvider) здесь существуют исключительно в учебных целях.
*  Можно обойтись без него и получать строки в compose-функциях через метод stringResource()
* */
private fun getLocalizedString(resource: Int): String {
return stringResourcesProvider.getString(resource)
}