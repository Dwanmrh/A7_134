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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dwan.ta_pam.ui.customwidget.CustomTopAppBar
import com.dwan.ta_pam.ui.navigation.DestinasiNavigasi
import com.dwan.ta_pam.ui.viewmodel.PenyediaViewModel
import com.dwan.ta_pam.ui.viewmodel.UpdateSUiEvent
import com.dwan.ta_pam.ui.viewmodel.UpdateSUiState
import com.dwan.ta_pam.ui.viewmodel.UpdateSesiTerapiViewModel
import kotlinx.coroutines.launch

object DestinasiUpdateSesiTerapi : DestinasiNavigasi {
    override val route = "update_sesi_terapi"
    override val titleRes = "Update Sesi Terapi"
    const val id_sesi = "id_sesi"
    val routeWithArgs = "${DestinasiUpdateSesiTerapi.route}/{$id_sesi}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateSesiTerapiScreen(
    id_sesi: String,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UpdateSesiTerapiViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // Tambahkan LaunchedEffect untuk mengambil data saat layar dibuka
    LaunchedEffect(id_sesi) {
        viewModel.getSesiTerapiById(id_sesi)
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomTopAppBar(
                title = DestinasiUpdateSesiTerapi.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        UpdateBodySesiTerapi(
            updateSUiState = viewModel.updateSUiState,
            onSesiTerapiValueChange = viewModel::updateSesiTerapiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateSesiTerapi()
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
fun UpdateBodySesiTerapi(
    updateSUiState: UpdateSUiState,
    onSesiTerapiValueChange: (UpdateSUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ) {
        FormInputSesiTerapi(
            updateSUiEvent = updateSUiState.updateSUiEvent,
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
            Text(text = "Update")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormInputSesiTerapi(
    updateSUiEvent: UpdateSUiEvent,
    modifier: Modifier = Modifier,
    onValueChange: (UpdateSUiEvent) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = updateSUiEvent.tanggal_sesi,
            onValueChange = { onValueChange(updateSUiEvent.copy(tanggal_sesi = it)) },
            label = { Text("Tanggal Sesi Terapi") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = updateSUiEvent.id_sesi,
            onValueChange = { onValueChange(updateSUiEvent.copy(id_sesi = it)) },
            label = { Text("ID Sesi Terapi") },
            modifier = Modifier.fillMaxWidth(),
            enabled = true, // Tidak diizinkan mengubah ID Pasien
            singleLine = true
        )
        OutlinedTextField(
            value = updateSUiEvent.id_pasien,
            onValueChange = { onValueChange(updateSUiEvent.copy(id_pasien = it)) },
            label = { Text("ID Pasien") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = updateSUiEvent.id_terapis,
            onValueChange = { onValueChange(updateSUiEvent.copy(id_terapis = it)) },
            label = { Text("ID Terapis") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = updateSUiEvent.id_jenis_terapi,
            onValueChange = { onValueChange(updateSUiEvent.copy(id_jenis_terapi = it)) },
            label = { Text("ID Jenis Terapi") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = updateSUiEvent.catatan_sesi,
            onValueChange = { onValueChange(updateSUiEvent.copy(catatan_sesi = it)) },
            label = { Text("Catatan Sesi") },
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