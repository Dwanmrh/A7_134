package com.dwan.ta_pam.ui.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dwan.ta_pam.ui.customwidget.CustomTopAppBar
import com.dwan.ta_pam.ui.navigation.DestinasiNavigasi
import com.dwan.ta_pam.ui.viewmodel.InsertPasienViewModel
import com.dwan.ta_pam.ui.viewmodel.PenyediaViewModel
import kotlinx.coroutines.launch

// Membuat objek `DestinasiEntry` yang mengimplementasikan interface `DestinasiNavigasi`
object DestinasiEntryPasien : DestinasiNavigasi {

    // Mendefinisikan properti `route` yang mengacu pada string "item_entry"
    override val route = "entry_pasien"

    // Mendefinisikan properti `titleRes` yang mengacu pada string "Entry Pas"
    override val titleRes = "Insert Pasien"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryPasScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertPasienViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomTopAppBar(
                title = DestinasiEntryPasien.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        EntryBodyPas(
            insertUiState = viewModel.insertUiState,
            onPasienValueChange = viewModel::updateInsertPasState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.insertPas()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}
