package com.dwan.ta_pam.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dwan.ta_pam.ui.customwidget.CustomTopAppBar
import com.dwan.ta_pam.ui.navigation.DestinasiNavigasi
import com.dwan.ta_pam.ui.viewmodel.InsertJUiEvent
import com.dwan.ta_pam.ui.viewmodel.InsertJUiState
import com.dwan.ta_pam.ui.viewmodel.InsertJenisTerapiViewModel
import com.dwan.ta_pam.ui.viewmodel.PenyediaViewModel
import kotlinx.coroutines.launch

// Membuat objek `DestinasiEntryTerapis` yang mengimplementasikan interface `DestinasiNavigasi`
object DestinasiEntryJenisTerapi : DestinasiNavigasi {

    // Mendefinisikan properti `route` yang mengacu pada string "item_entry"
    override val route = "entry_jenis_terapi"

    // Mendefinisikan properti `titleRes` yang mengacu pada string "Entry Pas"
    override val titleRes = "Insert Jenis Terapi"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryJenisTerapiScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertJenisTerapiViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomTopAppBar(
                title = DestinasiEntryJenisTerapi.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        },
    ) { innerPadding ->
        EntryBodyJenisTerapi(
            insertJUiState = viewModel.insertJUiState,
            onJenisTerapiValueChange = viewModel::updateInsertJnsState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.insertJns()
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

@Composable
fun EntryBodyJenisTerapi(
    insertJUiState: InsertJUiState,
    onJenisTerapiValueChange: (InsertJUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ) {
        FormInputJenisTerapi(
            insertJUiEvent = insertJUiState.insertJUiEvent,
            onValueChange = onJenisTerapiValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF8B0000), // Warna merah gelap
                contentColor = Color.White // Warna teks putih
            )
        ) {
            Text(text = "Simpan")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormInputJenisTerapi(
    insertJUiEvent: InsertJUiEvent,
    modifier: Modifier = Modifier,
    onValueChange: (InsertJUiEvent) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = insertJUiEvent.nama_jenis_terapi,
            onValueChange = { onValueChange(insertJUiEvent.copy(nama_jenis_terapi = it)) },
            label = { Text("Nama Jenis Terapi") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = insertJUiEvent.id_jenis_terapi,
            onValueChange = { onValueChange(insertJUiEvent.copy(id_jenis_terapi = it)) },
            label = { Text("ID Jenis Terapi") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = insertJUiEvent.deskripsi_terapi,
            onValueChange = { onValueChange(insertJUiEvent.copy(deskripsi_terapi = it)) },
            label = { Text("Deskripsi Terapi") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        // Menampilkan pesan "Harap Mengisi Semua Data!" jika input aktif
        if (enabled) {
            Text(
                text = "Harap Mengisi Semua Data!",
                modifier = Modifier.padding(12.dp),
                color = Color.Blue
            )
        }
        // Garis pemisah tebal
        Divider(
            thickness = 8.dp,
            modifier = Modifier.padding(12.dp)
        )
    }
}