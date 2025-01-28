package com.dwan.ta_pam.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dwan.ta_pam.ui.view.DestinasiEntryJenisTerapi
import com.dwan.ta_pam.ui.view.DestinasiEntryPasien
import com.dwan.ta_pam.ui.view.DestinasiEntrySesiTerapi
import com.dwan.ta_pam.ui.view.DestinasiEntryTerapis
import com.dwan.ta_pam.ui.view.DestinasiHome
import com.dwan.ta_pam.ui.view.DestinasiJenisTerapi
import com.dwan.ta_pam.ui.view.DestinasiSesiTerapi
import com.dwan.ta_pam.ui.view.DestinasiTerapis
import com.dwan.ta_pam.ui.view.DestinasiUpdateJenisTerapi
import com.dwan.ta_pam.ui.view.DestinasiUpdatePasien
import com.dwan.ta_pam.ui.view.DestinasiUpdateSesiTerapi
import com.dwan.ta_pam.ui.view.DestinasiUpdateSesiTerapi.id_sesi
import com.dwan.ta_pam.ui.view.DestinasiUpdateTerapis
import com.dwan.ta_pam.ui.view.DetailJnsScreen
import com.dwan.ta_pam.ui.view.DetailPasScreen
import com.dwan.ta_pam.ui.view.DetailSesiScreen
import com.dwan.ta_pam.ui.view.DetailTerScreen
import com.dwan.ta_pam.ui.view.EntryJenisTerapiScreen
import com.dwan.ta_pam.ui.view.EntryPasScreen
import com.dwan.ta_pam.ui.view.EntrySesiTerapiScreen
import com.dwan.ta_pam.ui.view.EntryTerapisScreen
import com.dwan.ta_pam.ui.view.HomeScreen
import com.dwan.ta_pam.ui.view.JenisTerapiScreen
import com.dwan.ta_pam.ui.view.SesiTerapiScreen
import com.dwan.ta_pam.ui.view.TerapisScreen
import com.dwan.ta_pam.ui.view.UpdateJenisTerapiScreen
import com.dwan.ta_pam.ui.view.UpdatePasScreen
import com.dwan.ta_pam.ui.view.UpdateSesiTerapiScreen
import com.dwan.ta_pam.ui.view.UpdateTerapisScreen

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
                navigateToTerapis = {
                    navController.navigate(DestinasiTerapis.route)
                },
                navigateToJenisTerapi = {
                    navController.navigate(DestinasiJenisTerapi.route)
                },
                navigateToSesiTerapi = {
                    navController.navigate((DestinasiSesiTerapi.route))
                },
                onDetailClick = { id_pasien ->
                    navController.navigate("detail_pasien/$id_pasien")
                },
                navigateToUpdatePasien = { id_pasien ->
                    navController.navigate("${DestinasiUpdatePasien.route}/$id_pasien")
                },
            )
        }
        // Halaman Entry Pasien
        composable(DestinasiEntryPasien.route) {
            EntryPasScreen(
                navigateBack = {
                    navController.navigate(DestinasiHome.route)
                }
            )
        }
        // Halaman Update Pasien
        composable(
            route = DestinasiUpdatePasien.routeWithArgs,
            arguments = listOf(navArgument(DestinasiUpdatePasien.id_pasien) { type = NavType.StringType })
        ) { backStackEntry ->
            val id_pasien = backStackEntry.arguments?.getString(DestinasiUpdatePasien.id_pasien) ?: ""
            UpdatePasScreen(
                id_pasien = id_pasien,
                navigateBack = {
                    navController.navigate(DestinasiHome.route)
                }
            )
        }
        // Halaman Detail Pasien
        composable(
            route = "detail_pasien/{id_pasien}",
            arguments = listOf(
                navArgument("id_pasien") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val id_pasien = backStackEntry.arguments?.getString("id_pasien") ?: ""
            DetailPasScreen(
                id_pasien = id_pasien,
                navigateToPasien = {
                    navController.navigate(DestinasiHome.route)
                },
                navigateToTerapis = {
                    navController.navigate(DestinasiTerapis.route)
                },
                navigateToJenisTerapi = {
                    navController.navigate(DestinasiJenisTerapi.route)
                },
                navigateToSesiTerapi = {
                    navController.navigate((DestinasiSesiTerapi.route))
                },
                navigateBack = { navController.popBackStack() },
                navigateSesi = { navController.navigate(DestinasiEntrySesiTerapi.route) }
            )
        }
        // Halaman Home Terapis
        composable(DestinasiTerapis.route) {
            TerapisScreen(
                navigateToTerapisEntry = {
                    navController.navigate(DestinasiEntryTerapis.route)
                },
                navigateToPasien = {
                    navController.navigate(DestinasiHome.route)
                },
                navigateToTerapis = {
                    navController.navigate(DestinasiTerapis.route)
                },
                navigateToJenisTerapi = {
                    navController.navigate(DestinasiJenisTerapi.route)
                },
                navigateToSesiTerapi = {
                    navController.navigate(DestinasiSesiTerapi.route)
                },
                navigateToUpdateTerapis = { id_terapis ->
                    navController.navigate("${DestinasiUpdateTerapis.route}/$id_terapis")
                },
                onDetailTerapisClick = { id_terapis ->
                    navController.navigate("detail_terapis/$id_terapis")
                }
            )
        }
        // Halaman Entry Terapis
        composable(DestinasiEntryTerapis.route) {
            EntryTerapisScreen(
                navigateBack = {
                    navController.navigate(DestinasiTerapis.route)
                }
            )
        }
        // Halaman Update Terapis
        composable(
            route = DestinasiUpdateTerapis.routeWithArgs,
            arguments = listOf(navArgument(DestinasiUpdateTerapis.id_terapis) { type = NavType.StringType })
        ) { backStackEntry ->
            val id_terapis = backStackEntry.arguments?.getString(DestinasiUpdateTerapis.id_terapis) ?: ""
            UpdateTerapisScreen(
                id_terapis = id_terapis,
                navigateBack = {
                    navController.navigate(DestinasiTerapis.route)
                }
            )
        }
        // Halaman Detail Terapis
        composable(
            route = "detail_terapis/{id_terapis}",
            arguments = listOf(navArgument("id_terapis") { type = NavType.StringType })
        ) { backStackEntry ->
            val id_terapis = backStackEntry.arguments?.getString("id_terapis") ?: ""
            DetailTerScreen(
                id_terapis = id_terapis,
                navigateToPasien = {
                    navController.navigate(DestinasiHome.route)
                },
                navigateToTerapis = {
                    navController.navigate(DestinasiTerapis.route)
                },
                navigateToJenisTerapi = {
                    navController.navigate(DestinasiJenisTerapi.route)
                },
                navigateToSesiTerapi = {
                    navController.navigate((DestinasiSesiTerapi.route))
                },
                navigateBack = {
                    navController.popBackStack()
                },
                navigateToTerapisEntry = {
                    navController.navigate(DestinasiEntryTerapis.route)
                },
            )
        }
        // Halaman Home Jenis Terapi
        composable(DestinasiJenisTerapi.route) {
            JenisTerapiScreen(
                navigateToJenisTerapiEntry = {
                    navController.navigate(DestinasiEntryJenisTerapi.route)
                },
                navigateToPasien = {
                    navController.navigate(DestinasiHome.route)
                },
                navigateToTerapis = {
                    navController.navigate(DestinasiTerapis.route)
                },
                navigateToJenisTerapi = {
                    navController.navigate(DestinasiJenisTerapi.route)
                },
                navigateToSesiTerapi = {
                    navController.navigate(DestinasiSesiTerapi.route)
                },
                navigateToUpdateJenisTerapi = { id_jenis_terapi ->
                    navController.navigate("${DestinasiUpdateJenisTerapi.route}/$id_jenis_terapi")
                },
                onDetailJenisTerapiClick = { id_jenis_terapi ->
                    navController.navigate("detail_jenis_terapi/$id_jenis_terapi")
                }
            )
        }
        // Halaman Entry Jenis Terapi
        composable(DestinasiEntryJenisTerapi.route) {
            EntryJenisTerapiScreen(
                navigateBack = {
                    navController.navigate(DestinasiJenisTerapi.route)
                }
            )
        }
        // Halaman Update Jenis Terapi
        composable(
            route = DestinasiUpdateJenisTerapi.routeWithArgs,
            arguments = listOf(navArgument(DestinasiUpdateJenisTerapi.id_jenis_terapi) { type = NavType.StringType })
        ) { backStackEntry ->
            val id_jenis_terapi = backStackEntry.arguments?.getString(DestinasiUpdateJenisTerapi.id_jenis_terapi) ?: ""
            UpdateJenisTerapiScreen(
                id_jenis_terapi = id_jenis_terapi,
                navigateBack = {
                    navController.navigate(DestinasiJenisTerapi.route)
                }
            )
        }
        // Halaman Detail Jenis Terapi
        composable(
            route = "detail_jenis_terapi/{id_jenis_terapi}",
            arguments = listOf(navArgument("id_jenis_terapi") { type = NavType.StringType })
        ) { backStackEntry ->
            val id_jenis_terapi = backStackEntry.arguments?.getString("id_jenis_terapi") ?: ""
            DetailJnsScreen(
                id_jenis_terapi = id_jenis_terapi,
                navigateToPasien = {
                    navController.navigate(DestinasiHome.route)
                },
                navigateToTerapis = {
                    navController.navigate(DestinasiTerapis.route)
                },
                navigateToJenisTerapi = {
                    navController.navigate(DestinasiJenisTerapi.route)
                },
                navigateToSesiTerapi = {
                    navController.navigate((DestinasiSesiTerapi.route))
                },
                navigateBack = {
                    navController.popBackStack()
                },
                navigateToJenisTerapiEntry = {
                    navController.navigate(DestinasiEntryJenisTerapi.route)
                },
            )
        }
        // Halaman Home Sesi Terapi
        composable(DestinasiSesiTerapi.route) {
            SesiTerapiScreen(
                navigateToSesiTerapiEntry = {
                    navController.navigate(DestinasiEntrySesiTerapi.route)
                },
                navigateToPasien = {
                    navController.navigate(DestinasiHome.route)
                },
                navigateToTerapis = {
                    navController.navigate(DestinasiTerapis.route)
                },
                navigateToJenisTerapi = {
                    navController.navigate(DestinasiJenisTerapi.route)
                },
                navigateToSesiTerapi = {
                    navController.navigate(DestinasiSesiTerapi.route)
                },
                navigateToUpdateSesiTerapi = { id_sesi ->
                    navController.navigate("${DestinasiUpdateSesiTerapi.route}/$id_sesi")
                },
                onDetailSesiTerapiClick = { id_sesi ->
                    navController.navigate("detail_sesi_terapi/$id_sesi")
                }
            )
        }
        // Halaman Entry Sesi Terapi
        composable(DestinasiEntrySesiTerapi.route) {
            EntrySesiTerapiScreen(
                navigateBack = {
                    navController.navigate(DestinasiSesiTerapi.route)
                }
            )
        }
        // Halaman Update Sesi Terapi
        composable(
            route = DestinasiUpdateSesiTerapi.routeWithArgs,
            arguments = listOf(navArgument(DestinasiUpdateSesiTerapi.id_sesi) { type = NavType.StringType })
        ) { backStackEntry ->
            val id_sesi = backStackEntry.arguments?.getString(DestinasiUpdateSesiTerapi.id_sesi) ?: ""
            UpdateSesiTerapiScreen(
                id_sesi = id_sesi,
                navigateBack = {
                    navController.navigate(DestinasiSesiTerapi.route)
                }
            )
        }
        // Halaman Detail Sesi Terapi
        composable(
            route = "detail_sesi_terapi/{id_sesi}",
            arguments = listOf(navArgument("id_sesi") { type = NavType.StringType })
        ) { backStackEntry ->
            val id_sesi = backStackEntry.arguments?.getString("id_sesi") ?: ""
            DetailSesiScreen(
                id_sesi = id_sesi,
                navigateToPasien = {
                    navController.navigate(DestinasiHome.route)
                },
                navigateToTerapis = {
                    navController.navigate(DestinasiTerapis.route)
                },
                navigateToJenisTerapi = {
                    navController.navigate(DestinasiJenisTerapi.route)
                },
                navigateToSesiTerapi = {
                    navController.navigate((DestinasiSesiTerapi.route))
                },
                navigateBack = {
                    navController.popBackStack()
                },
                navigateToSesiTerapiEntry = {
                    navController.navigate(DestinasiEntrySesiTerapi.route)
                }
            )
        }
    }
}