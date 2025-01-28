package com.dwan.ta_pam.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dwan.ta_pam.ui.customwidget.CustomTopAppBar
import com.dwan.ta_pam.ui.navigation.DestinasiNavigasi
import com.dwan.ta_pam.ui.viewmodel.InsertSUiEvent
import com.dwan.ta_pam.ui.viewmodel.InsertSUiState
import com.dwan.ta_pam.ui.viewmodel.InsertSesiTerapiViewModel
import com.dwan.ta_pam.ui.viewmodel.PenyediaViewModel
import kotlinx.coroutines.launch

// Membuat objek `DestinasiEntryTerapis` yang mengimplementasikan interface `DestinasiNavigasi`
object DestinasiEntrySesiTerapi : DestinasiNavigasi {

    // Mendefinisikan properti `route` yang mengacu pada string "item_entry"
    override val route = "entry_sesi_terapi"

    // Mendefinisikan properti `titleRes` yang mengacu pada string "Entry Pas"
    override val titleRes = "Insert Sesi Terapi"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntrySesiTerapiScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertSesiTerapiViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomTopAppBar(
                title = DestinasiEntrySesiTerapi.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        },
    ) { innerPadding ->
        EntryBodySesiTerapi(
            insertSUiState = viewModel.insertSUiState,
            onSesiTerapiValueChange = viewModel::updateInsertSesiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.insertSesi()
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
fun EntryBodySesiTerapi(
    insertSUiState: InsertSUiState,
    onSesiTerapiValueChange: (InsertSUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ) {
        FormInputSesiTerapi(
            insertSUiEvent = insertSUiState.insertSUiEvent,
            onValueChange = onSesiTerapiValueChange,
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
fun FormInputSesiTerapi(
    insertSUiEvent: InsertSUiEvent,
    modifier: Modifier = Modifier,
    onValueChange: (InsertSUiEvent) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = insertSUiEvent.tanggal_sesi,
            onValueChange = { onValueChange(insertSUiEvent.copy(tanggal_sesi = it)) },
            label = { Text("Tanggal Sesi") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = insertSUiEvent.id_sesi,
            onValueChange = { onValueChange(insertSUiEvent.copy(id_sesi = it)) },
            label = { Text("ID Sesi Terapi") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = insertSUiEvent.id_pasien,
            onValueChange = { onValueChange(insertSUiEvent.copy(id_pasien = it)) },
            label = { Text("ID Pasien") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = insertSUiEvent.id_terapis,
            onValueChange = { onValueChange(insertSUiEvent.copy(id_terapis = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            label = { Text("ID Terapis") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = insertSUiEvent.id_jenis_terapi,
            onValueChange = { onValueChange(insertSUiEvent.copy(id_jenis_terapi = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            label = { Text("ID Jenis Terapi") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = insertSUiEvent.catatan_sesi,
            onValueChange = { onValueChange(insertSUiEvent.copy(catatan_sesi = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            label = { Text("Catatan Sesi Terapi") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        // Menampilkan pesan "Isi Semua Data!" jika input aktif
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