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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dwan.ta_pam.ui.customwidget.CustomTopAppBar
import com.dwan.ta_pam.ui.navigation.DestinasiNavigasi
import com.dwan.ta_pam.ui.viewmodel.PenyediaViewModel
import com.dwan.ta_pam.ui.viewmodel.UpdateTUiEvent
import com.dwan.ta_pam.ui.viewmodel.UpdateTUiState
import com.dwan.ta_pam.ui.viewmodel.UpdateTerapisViewModel
import kotlinx.coroutines.launch

object DestinasiUpdateTerapis : DestinasiNavigasi {
    override val route = "update_terapis"
    override val titleRes = "Update Terapis"
    const val id_terapis = "id_terapis"
    val routeWithArgs = "${DestinasiUpdateTerapis.route}/{$id_terapis}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateTerapisScreen(
    id_terapis: String,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UpdateTerapisViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // Tambahkan LaunchedEffect untuk mengambil data saat layar dibuka
    LaunchedEffect(id_terapis) {
        viewModel.getTerapisById(id_terapis)
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomTopAppBar(
                title = DestinasiUpdateTerapis.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        UpdateBodyTerapis(
            updateTUiState = viewModel.updateTUiState,
            onTerapisValueChange = viewModel::updateTerapisState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateTerapis()
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
fun UpdateBodyTerapis(
    updateTUiState: UpdateTUiState,
    onTerapisValueChange: (UpdateTUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ) {
        FormInputTerapis(
            updateTUiEvent = updateTUiState.updateTUiEvent,
            onValueChange = onTerapisValueChange,
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
            Text(text = "Update")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormInputTerapis(
    updateTUiEvent: UpdateTUiEvent,
    modifier: Modifier = Modifier,
    onValueChange: (UpdateTUiEvent) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = updateTUiEvent.nama_terapis,
            onValueChange = { onValueChange(updateTUiEvent.copy(nama_terapis = it)) },
            label = { Text("Nama Terapis") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = updateTUiEvent.id_terapis,
            onValueChange = { onValueChange(updateTUiEvent.copy(id_terapis = it)) },
            label = { Text("ID Terapis") },
            modifier = Modifier.fillMaxWidth(),
            enabled = true, // Tidak diizinkan mengubah ID Pasien
            singleLine = true
        )
        OutlinedTextField(
            value = updateTUiEvent.spesialisasi,
            onValueChange = { onValueChange(updateTUiEvent.copy(spesialisasi = it)) },
            label = { Text("Spesialisasi") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = updateTUiEvent.nomor_izin_praktik,
            onValueChange = { onValueChange(updateTUiEvent.copy(nomor_izin_praktik = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            label = { Text("Nomor Izin Praktik") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        Divider(
            thickness = 8.dp,
            modifier = Modifier.padding(12.dp)
        )
    }
}