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
        // Halaman Home Pasien
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
                onDetailClick = { id_pasien ->
                    // Navigasi ke halaman detail (jika diperlukan)
                },
                navigateToUpdatePasien = { id_pasien ->
                    // Navigasi ke halaman update dengan menyertakan id_pasien sebagai argumen
                    navController.navigate("${DestinasiUpdatePasien.route}/$id_pasien")
                }
            )
        }
        // Halaman Entry Pasien
        composable(DestinasiEntryPasien.route) {
            EntryPasScreen(
                navigateBack = {
                    navController.popBackStack() // Kembali ke halaman sebelumnya
                }
            )
        }
        // Halaman Update Pasien
        composable(
            route = DestinasiUpdatePasien.routeWithArgs,
            arguments = listOf(navArgument(DestinasiUpdatePasien.id_pasien) { type = NavType.StringType })
        ) { backStackEntry ->
            // Ambil id_pasien dari argumen navigasi
            val id_pasien = backStackEntry.arguments?.getString(DestinasiUpdatePasien.id_pasien) ?: ""

            UpdatePasScreen(
                id_pasien = id_pasien, // Teruskan id_pasien ke UpdatePasScreen
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}