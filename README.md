# CeloAndroidTest
This is a project using MVI architecture. UserAcitivty + UserFragment+ViewUserFragment. The project follows a lot of jetpack things.

User--interacts-->Intent--changes-->State--updates-->View--seen-->User 

StateEvent describes a event trigger that may not be emitted by user. Such as UserSearchEvent,RestoreUserListFromCache.

ViewState describes a module state that contain all the cases views including. For example, the user module,will contain UserFields,ViewUserFields for the two fragment.

DataState descibes the data state, like api or cache feedback, which including 3 states (data, loading,error)

So the flow, stateEvent set or change triggers viewmodel action then notify repository to get new dataState, once get it, then update ViewSate. 

Here is some introduction.

This project uses below technical details:
Dagger as di componenet. 
Retrofit as network framework and Coroutine with job to deal with task details.
Room and sharepreference as persistence storage.
Navigation graph to deal with fragment action
Backstack to handle fragment mananagement
NetworkBoundResource object to handle all the cases the repository will do, such as load cache ,handle api, update db, etc.



