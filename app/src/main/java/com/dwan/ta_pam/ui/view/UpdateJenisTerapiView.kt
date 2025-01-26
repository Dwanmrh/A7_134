package com.dwan.ta_pam.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dwan.ta_pam.ui.customwidget.CustomTopAppBar
import com.dwan.ta_pam.ui.navigation.DestinasiNavigasi
import com.dwan.ta_pam.ui.viewmodel.PenyediaViewModel
import com.dwan.ta_pam.ui.viewmodel.UpdateJUiEvent
import com.dwan.ta_pam.ui.viewmodel.UpdateJUiState
import com.dwan.ta_pam.ui.viewmodel.UpdateJenisTerapiViewModel
import com.dwan.ta_pam.ui.viewmodel.UpdateTUiEvent
import com.dwan.ta_pam.ui.viewmodel.UpdateTUiState
import com.dwan.ta_pam.ui.viewmodel.UpdateTerapisViewModel
import kotlinx.coroutines.launch

object DestinasiUpdateJenisTerapi : DestinasiNavigasi {
    override val route = "update_jenis_terapi"
    override val titleRes = "Update Jenis Terapi"
    const val id_jenis_terapi = "id_jenis_terapi"
    val routeWithArgs = "${DestinasiUpdateJenisTerapi.route}/{$id_jenis_terapi}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateJenisTerapiScreen(
    id_jenis_terapi: String,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UpdateJenisTerapiViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // Tambahkan LaunchedEffect untuk mengambil data saat layar dibuka
    LaunchedEffect(id_jenis_terapi) {
        viewModel.getJenisTerapiById(id_jenis_terapi)
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomTopAppBar(
                title = DestinasiUpdateJenisTerapi.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        UpdateBodyJenisTerapi(
            updateJUiState = viewModel.updateJUiState,
            onJenisTerapiValueChange = viewModel::updateJenisTerapiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateJenisTerapi()
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
fun UpdateBodyJenisTerapi(
    updateJUiState: UpdateJUiState,
    onJenisTerapiValueChange: (UpdateJUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ) {
        FormInputJenisTerapi(
            updateJUiEvent = updateJUiState.updateJUiEvent,
            onValueChange = onJenisTerapiValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Update")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormInputJenisTerapi(
    updateJUiEvent: UpdateJUiEvent,
    modifier: Modifier = Modifier,
    onValueChange: (UpdateJUiEvent) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = updateJUiEvent.nama_jenis_terapi,
            onValueChange = { onValueChange(updateJUiEvent.copy(nama_jenis_terapi = it)) },
            label = { Text("Nama Jenis Terapi") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = updateJUiEvent.id_jenis_terapi,
            onValueChange = { onValueChange(updateJUiEvent.copy(id_jenis_terapi = it)) },
            label = { Text("ID Jenis Terapi") },
            modifier = Modifier.fillMaxWidth(),
            enabled = true, // Tidak diizinkan mengubah ID Pasien
            singleLine = true
        )
        OutlinedTextField(
            value = updateJUiEvent.deskripsi_terapi,
            onValueChange = { onValueChange(updateJUiEvent.copy(deskripsi_terapi = it)) },
            label = { Text("Deskripsi Terapi") },
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