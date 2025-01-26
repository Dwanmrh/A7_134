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
import com.dwan.ta_pam.model.JenisTerapi
import com.dwan.ta_pam.model.Terapis
import com.dwan.ta_pam.ui.customwidget.CustomTopAppBar
import com.dwan.ta_pam.ui.customwidget.MenuButton
import com.dwan.ta_pam.ui.navigation.DestinasiNavigasi
import com.dwan.ta_pam.ui.viewmodel.HomeJUiState
import com.dwan.ta_pam.ui.viewmodel.HomeTUiState
import com.dwan.ta_pam.ui.viewmodel.JenisTerapiViewModel
import com.dwan.ta_pam.ui.viewmodel.PenyediaViewModel
import com.dwan.ta_pam.ui.viewmodel.TerapisViewModel

// Objek untuk mendefinisikan rute dan judul layar home
object DestinasiJenisTerapi : DestinasiNavigasi {
    override val route = "jenis_terapi" // Rute navigasi layar home
    override val titleRes = "Home Jenis Terapi"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JenisTerapiScreen(
    navigateToJenisTerapiEntry: () -> Unit, // Navigasi ke layar tambah data
    navigateToPasien: () -> Unit, // Navigasi ke halaman Pasien
    navigateToTerapis: () -> Unit, // Navigasi ke halaman Terapis
    navigateToJenisTerapi: () -> Unit, // Navigasi ke halaman Jenis Terapi
    navigateToSesiTerapi: () -> Unit, // Navigasi ke halaman Sesi Terapi
    modifier: Modifier = Modifier,
    navigateToUpdateJenisTerapi: (String) -> Unit, // Navigasi ke halaman update
    onDetailJenisTerapiClick: (String) -> Unit,
    viewModel: JenisTerapiViewModel = viewModel(factory = PenyediaViewModel.Factory) // ViewModel untuk mengelola data
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(MaterialTheme.colorScheme.background),
        topBar = {
            CustomTopAppBar( // Toolbar dengan tombol refresh
                title = DestinasiJenisTerapi.titleRes,
                canNavigateBack = false,
                scrollBehavior = scrollBehavior,
                onRefresh = {
                    viewModel.getJns() // Memuat ulang data terapis
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToJenisTerapiEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(18.dp),
                contentColor = Color.White,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Terapis")
            }
        },
        // Menambahkan MenuButton di bagian bawah FAB
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
        // Menampilkan status data mahasiswa
        JenisTerapiStatus(
            homeJUiState = viewModel.jnsUiState,
            retryAction = { viewModel.getJns() },
            modifier = Modifier.padding(innerPadding),
            onDetailJenisTerapiClick = onDetailJenisTerapiClick,
            onDeleteClick = { jenis_terapi ->
                viewModel.deleteJns(jenis_terapi.id_jenis_terapi) // Menghapus data mahasiswa
                viewModel.getJns() // Memuat ulang data setelah dihapus
            },
            navigateToUpdateJenisTerapi = navigateToUpdateJenisTerapi
        )
    }
}

@Composable
fun JenisTerapiStatus(
    homeJUiState: HomeJUiState, // Status data mahasiswa
    retryAction: () -> Unit, // Aksi untuk memuat ulang
    navigateToUpdateJenisTerapi: (String) -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: (JenisTerapi) -> Unit = {},
    onDetailJenisTerapiClick: (String) -> Unit
) {
    when (homeJUiState) {
        is HomeJUiState.Loading -> JenisTerapiLoading(modifier = modifier.fillMaxSize()) // Menampilkan loading
        is HomeJUiState.Success -> {
            if (homeJUiState.jenisTerapi.isEmpty()) {
                // Tampilkan pesan jika data kosong
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Tidak ada data Jenis Terapi")
                }
            } else {
                // Tampilkan daftar mahasiswa
                JenisTerapiLayout(
                    jenisTerapi = homeJUiState.jenisTerapi,
                    modifier = modifier.fillMaxWidth(),
                    navigateToUpdateJenisTerapi = navigateToUpdateJenisTerapi,
                    onDetailJenisTerapiClick = { onDetailJenisTerapiClick(it.id_jenis_terapi) },
                    onDeleteClick = { onDeleteClick(it) },
                )
            }
        }
        is HomeJUiState.Error -> JenisTerapiError(retryAction, modifier = modifier.fillMaxSize()) // Tampilkan pesan error
    }
}

@Composable
fun JenisTerapiLoading(
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
fun JenisTerapiError(retryAction: () -> Unit, modifier: Modifier = Modifier) {
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
fun JenisTerapiLayout(
    jenisTerapi: List<JenisTerapi>,
    modifier: Modifier = Modifier,
    navigateToUpdateJenisTerapi: (String) -> Unit,
    onDetailJenisTerapiClick: (JenisTerapi) -> Unit,
    onDeleteClick: (JenisTerapi) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(jenisTerapi) { jenisTerapi ->
            JenisTerapiCard(
                jenisTerapi = jenisTerapi,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDetailJenisTerapiClick(jenisTerapi) },
                navigateToUpdateJenisTerapi = navigateToUpdateJenisTerapi, // Fungsi klik untuk update
                onDeleteClick = { onDeleteClick(jenisTerapi) }
            )
        }
    }
}

@Composable
fun JenisTerapiCard(
    jenisTerapi: JenisTerapi, // Data Terapis
    modifier: Modifier = Modifier,
    navigateToUpdateJenisTerapi: (String) -> Unit,
    onDeleteClick: (JenisTerapi) -> Unit = {}
) {
    // State untuk menampilkan dialog konfirmasi
    var deleteConfirmationRequired by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .shadow(8.dp, shape = MaterialTheme.shapes.medium), // Tambahkan bayangan
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary // Warna latar belakang kartu
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Gambar profil
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "Foto Jenis Terapi",
                modifier = Modifier
                    .size(48.dp)
                    .clip(MaterialTheme.shapes.small) // Bentuk lingkaran
            )

            Spacer(Modifier.width(16.dp)) // Jarak antar elemen

            // Informasi Jenis Terapi
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = jenisTerapi.nama_jenis_terapi,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondaryContainer // Warna teks
                )
                Text(
                    text = jenisTerapi.id_jenis_terapi,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
            }

            // Tombol Edit
            IconButton(
                onClick = { navigateToUpdateJenisTerapi(jenisTerapi.id_jenis_terapi) }, // Navigasi ke halaman update
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Jenis Terapi",
                    tint = MaterialTheme.colorScheme.error
                )
            }

            // Tombol hapus
            IconButton(
                onClick = { deleteConfirmationRequired = true },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error // Warna ikon
                )
            }
        }
    }

    // Dialog konfirmasi hapus
    if (deleteConfirmationRequired) {
        DeleteConfirmationDialog(
            onDeleteConfirm = {
                deleteConfirmationRequired = false
                onDeleteClick(jenisTerapi)
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