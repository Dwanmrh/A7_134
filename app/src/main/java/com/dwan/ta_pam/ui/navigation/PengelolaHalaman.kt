package com.dwan.ta_pam.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dwan.ta_pam.ui.view.DestinasiHome
import com.dwan.ta_pam.ui.view.HomeScreen

@Composable
fun PengelolaHalaman(navController: NavHostController = rememberNavController()) {

    // Mengatur navigasi antar layar menggunakan NavHost
    NavHost(
        navController = navController, // Controller navigasi untuk mengelola navigasi antar layar
        startDestination = DestinasiHome.route, // Layar awal adalah HomeScreen
        modifier = Modifier,
    ) {
        // Halaman Home
        composable(DestinasiHome.route) {
            HomeScreen(
                navigateToPasienEntry = {
                },
                navigateToPasien = {
                    navController.navigate(DestinasiHome.route) // Navigasi ke Halaman Pasien
                },
                navigateToTerapis = {
                },
                navigateToJenisTerapi = {
                },
                navigateToSesiTerapi = {
                },
                onDetailClick = {
                }
            )
        }
    }
}