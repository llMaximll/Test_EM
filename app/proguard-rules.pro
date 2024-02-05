# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontwarn com.github.llmaximll.sign_up.SignUpScreenKt
-dontwarn com.github.llmaximll.sign_up.SignUpViewModel
-dontwarn com.github.llmaximll.sign_up.SignUpViewModel_HiltModules_KeyModule_ProvideFactory
-dontwarn com.github.llmaximll.test_em.core.common.CoroutinesUtilsKt
-dontwarn com.github.llmaximll.test_em.core.common.LogsKt
-dontwarn com.github.llmaximll.test_em.core.common.ext.StringExtKt
-dontwarn com.github.llmaximll.test_em.core.common.repositories_abstract.CommonRepository
-dontwarn com.github.llmaximll.test_em.core.common.repositories_abstract.ItemRepository
-dontwarn com.github.llmaximll.test_em.core.common.repositories_abstract.UserRepository
-dontwarn com.github.llmaximll.test_em.core.common.theme.AppColors
-dontwarn com.github.llmaximll.test_em.core.common.theme.CustomTypography
-dontwarn com.github.llmaximll.test_em.core.common.theme.ThemeKt
-dontwarn com.github.llmaximll.test_em.data.local.daos.ItemDao
-dontwarn com.github.llmaximll.test_em.data.local.daos.UserDao
-dontwarn com.github.llmaximll.test_em.data.local.di.LocalModule_ProvideAppDbFactory
-dontwarn com.github.llmaximll.test_em.data.local.di.LocalModule_ProvideItemDaoFactory
-dontwarn com.github.llmaximll.test_em.data.local.di.LocalModule_ProvideUserDaoFactory
-dontwarn com.github.llmaximll.test_em.data.local.sources.ItemLocalDataSource
-dontwarn com.github.llmaximll.test_em.data.local.sources.ItemLocalDataSourceImpl
-dontwarn com.github.llmaximll.test_em.data.local.sources.UserLocalDataSource
-dontwarn com.github.llmaximll.test_em.data.local.sources.UserLocalDataSourceImpl
-dontwarn com.github.llmaximll.test_em.data.remote.api_services.ItemApiService
-dontwarn com.github.llmaximll.test_em.data.remote.di.RemoteModule_ProvideItemApiServiceFactory
-dontwarn com.github.llmaximll.test_em.data.remote.di.RemoteModule_ProvideRetrofitFactory
-dontwarn com.github.llmaximll.test_em.data.remote.sources.ItemRemoteDataSource
-dontwarn com.github.llmaximll.test_em.data.remote.sources.ItemRemoteDataSourceImpl
-dontwarn com.github.llmaximll.test_em.data.repositories.CommonRepositoryImpl
-dontwarn com.github.llmaximll.test_em.data.repositories.ItemRepositoryImpl
-dontwarn com.github.llmaximll.test_em.data.repositories.UserRepositoryImpl
-dontwarn com.github.llmaximll.test_em.features.cart.CartScreenKt
-dontwarn com.github.llmaximll.test_em.features.catalog.CatalogScreenKt
-dontwarn com.github.llmaximll.test_em.features.catalog.CatalogViewModel
-dontwarn com.github.llmaximll.test_em.features.catalog.CatalogViewModel_HiltModules_KeyModule_ProvideFactory
-dontwarn com.github.llmaximll.test_em.features.discount.DiscountScreenKt
-dontwarn com.github.llmaximll.test_em.features.favorite.FavoriteScreenKt
-dontwarn com.github.llmaximll.test_em.features.favorite.FavoriteViewModel
-dontwarn com.github.llmaximll.test_em.features.favorite.FavoriteViewModel_HiltModules_KeyModule_ProvideFactory
-dontwarn com.github.llmaximll.test_em.features.main.MainScreenKt
-dontwarn com.github.llmaximll.test_em.features.product_details.ProductDetailsScreenKt
-dontwarn com.github.llmaximll.test_em.features.product_details.ProductDetailsViewModel
-dontwarn com.github.llmaximll.test_em.features.product_details.ProductDetailsViewModel_HiltModules_KeyModule_ProvideFactory
-dontwarn com.github.llmaximll.test_em.features.profile.ProfileScreenKt
-dontwarn com.github.llmaximll.test_em.features.profile.ProfileViewModel
-dontwarn com.github.llmaximll.test_em.features.profile.ProfileViewModel_HiltModules_KeyModule_ProvideFactory