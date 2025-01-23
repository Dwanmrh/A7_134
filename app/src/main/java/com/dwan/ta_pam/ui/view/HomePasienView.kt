package com.dwan.ta_pam.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dwan.ta_pam.R
import com.dwan.ta_pam.model.Pasien
import com.dwan.ta_pam.ui.customwidget.CustomTopAppBar
import com.dwan.ta_pam.ui.customwidget.MenuButton
import com.dwan.ta_pam.ui.navigation.DestinasiNavigasi
import com.dwan.ta_pam.ui.viewmodel.HomePasienViewModel
import com.dwan.ta_pam.ui.viewmodel.HomeUiState
import com.dwan.ta_pam.ui.viewmodel.PenyediaViewModel

object DestinasiHome : DestinasiNavigasi {
    override val route = "home"
    override val titleRes = "Home Pasien"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToPasienEntry: () -> Unit, // Navigasi ke layar tambah data
    navigateToPasien: () -> Unit, // Navigasi ke halaman Pasien
    navigateToTerapis: () -> Unit, // Navigasi ke halaman Terapis
    navigateToJenisTerapi: () -> Unit, // Navigasi ke halaman Jenis Terapi
    navigateToSesiTerapi: () -> Unit, // Navigasi ke halaman Sesi Terapi
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit = {}, // Navigasi ke halaman detail
    navigateToUpdatePasien: (String) -> Unit,
    viewModel: HomePasienViewModel = viewModel(factory = PenyediaViewModel.Factory) // ViewModel untuk mengelola data
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(MaterialTheme.colorScheme.background),
        topBar = {
            CustomTopAppBar(
                title = DestinasiHome.titleRes,
                canNavigateBack = false,
                scrollBehavior = scrollBehavior,
                onRefresh = {
                    viewModel.getPas()
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToPasienEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(18.dp),
                contentColor = Color.White,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Pasien")
            }
        },
        bottomBar = {
            MenuButton(
                onPasienClick = navigateToPasien,
                onTerapisClick = navigateToTerapis,
                onJenisTerapiClick = navigateToJenisTerapi,
                onSesiTerapiClick = navigateToSesiTerapi,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
    ) { innerPadding ->
        HomeStatus(
            homeUiState = viewModel.pasUiState,
            retryAction = { viewModel.getPas() },
            modifier = Modifier.padding(innerPadding),
            onDetailClick = onDetailClick,
            onDeleteClick = { pasien ->
                viewModel.deletePas(pasien.id_pasien)
                viewModel.getPas()
            },
            navigateToUpdatePasien = navigateToUpdatePasien
        )
    }
}

@Composable
fun HomeStatus(
    homeUiState: HomeUiState,
    retryAction: () -> Unit,
    navigateToUpdatePasien: (String) -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: (Pasien) -> Unit = {},
    onDetailClick: (String) -> Unit
) {
    when (homeUiState) {
        is HomeUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize()) // Menampilkan loading
        is HomeUiState.Success -> {
            if (homeUiState.pasien.isEmpty()) {
                // Tampilkan pesan jika data kosong
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Tidak ada data Mahasiswa")
                }
            } else {
                // Tampilkan daftar mahasiswa
                PasLayout(
                    pasien = homeUiState.pasien,
                    modifier = modifier.fillMaxWidth(),
                    navigateToUpdatePasien = navigateToUpdatePasien,
                    onDetailClick = { onDetailClick(it.id_pasien) }, // Mengarahkan ke detail
                    onDeleteClick = { onDeleteClick(it) }
                )
            }
        }
        is HomeUiState.Error -> OnError(retryAction, modifier = modifier.fillMaxSize()) // Tampilkan pesan error
    }
}

@Composable
fun OnLoading(
    modifier: Modifier
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.size(70.dp), // Ukuran eksplisit di sini
            painter = painterResource(id = R.drawable.loading_img),
            contentDescription = stringResource(R.string.loading)
        )
    }
}

@Composable
fun OnError(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Composable
fun PasLayout(
    pasien: List<Pasien>,
    modifier: Modifier = Modifier,
    navigateToUpdatePasien: (String) -> Unit,
    onDetailClick: (Pasien) -> Unit,
    onDeleteClick: (Pasien) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(pasien) { pasien ->
            PasCard(
                pasien = pasien,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDetailClick(pasien) }, // Fungsi klik untuk detail
                navigateToUpdatePasien = navigateToUpdatePasien,
                onDeleteClick = { onDeleteClick(pasien) }
            )
        }
    }
}

@Composable
fun PasCard(
    pasien: Pasien,
    modifier: Modifier = Modifier,
    navigateToUpdatePasien: (String) -> Unit,
    onDeleteClick: (Pasien) -> Unit = {}
) {
    var deleteConfirmationRequired by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .shadow(8.dp, shape = MaterialTheme.shapes.medium),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Informasi Pasien
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = pasien.nama_pasien,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
                Text(
                    text = pasien.id_pasien,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
                Text(
                    text = pasien.alamat,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
            }

            // Tombol Edit
            IconButton(
                onClick = { navigateToUpdatePasien(pasien.id_pasien) }, // Navigasi ke halaman update
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Pasien",
                    tint = MaterialTheme.colorScheme.error
                )
            }

            // Tombol Hapus
            IconButton(
                onClick = { deleteConfirmationRequired = true },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }

    // Dialog Konfirmasi Hapus
    if (deleteConfirmationRequired) {
        DeleteConfirmationDialog(
            onDeleteConfirm = {
                deleteConfirmationRequired = false
                onDeleteClick(pasien)
            },
            onDeleteCancel = { deleteConfirmationRequired = false }
        )
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { onDeleteCancel() },
        title = { Text("Delete Data") },
        text = { Text("Apakah anda yakin ingin menghapus data ini?") },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = "Cancel")
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = "Yes")
            }
        }
    )
}