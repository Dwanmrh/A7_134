package com.dwan.ta_pam.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dwan.ta_pam.ui.view.DestinasiEntryPasien
import com.dwan.ta_pam.ui.view.DestinasiHome
import com.dwan.ta_pam.ui.view.DestinasiUpdatePasien
import com.dwan.ta_pam.ui.view.EntryPasScreen
import com.dwan.ta_pam.ui.view.HomeScreen
import com.dwan.ta_pam.ui.view.UpdatePasScreen

@Composable
fun PengelolaHalaman(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = DestinasiHome.route,
        modifier = Modifier,
    ) {
        composable(DestinasiHome.route) {
            HomeScreen(
                navigateToPasienEntry = {
                    navController.navigate(DestinasiEntryPasien.route)
                },
                navigateToPasien = {
                    navController.navigate(DestinasiHome.route)
                },
                navigateToTerapis = {},
                navigateToJenisTerapi = {},
                navigateToSesiTerapi = {},
                onDetailClick = {},
                navigateToUpdatePasien = { id_pasien ->
                    // Navigasi ke halaman update dengan menyertakan id_pasien sebagai argumen
                    navController.navigate("${DestinasiUpdatePasien.route}/$id_pasien")
                }
            )
        }

        composable(DestinasiEntryPasien.route) {
            EntryPasScreen(
                navigateBack = {
                    navController.navigate(DestinasiHome.route) {
                        popUpTo(DestinasiHome.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = DestinasiUpdatePasien.routeWithArgs,
            arguments = listOf(navArgument(DestinasiUpdatePasien.id_pasien) { type = NavType.StringType })
        ) { backStackEntry ->
            UpdatePasScreen(
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}
