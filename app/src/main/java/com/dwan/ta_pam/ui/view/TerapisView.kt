package com.dwan.ta_pam.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.ripple.rememberRipple
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
import com.dwan.ta_pam.model.Terapis
import com.dwan.ta_pam.ui.customwidget.CustomTopAppBar
import com.dwan.ta_pam.ui.customwidget.MenuButton
import com.dwan.ta_pam.ui.navigation.DestinasiNavigasi
import com.dwan.ta_pam.ui.viewmodel.HomeTUiState
import com.dwan.ta_pam.ui.viewmodel.PenyediaViewModel
import com.dwan.ta_pam.ui.viewmodel.TerapisViewModel

// Objek untuk mendefinisikan rute dan judul layar home
object DestinasiTerapis : DestinasiNavigasi {
    override val route = "terapis" // Rute navigasi layar home
    override val titleRes = "Home Terapis"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerapisScreen(
    navigateToTerapisEntry: () -> Unit, // Navigasi ke layar tambah data
    navigateToPasien: () -> Unit, // Navigasi ke halaman Pasien
    navigateToTerapis: () -> Unit, // Navigasi ke halaman Terapis
    navigateToJenisTerapi: () -> Unit, // Navigasi ke halaman Jenis Terapi
    navigateToSesiTerapi: () -> Unit, // Navigasi ke halaman Sesi Terapi
    modifier: Modifier = Modifier,
    navigateToUpdateTerapis: (String) -> Unit, // Navigasi ke halaman update
    onDetailTerapisClick: (String) -> Unit,
    viewModel: TerapisViewModel = viewModel(factory = PenyediaViewModel.Factory) // ViewModel untuk mengelola data
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(Color(0xFF121212)), // Warna latar belakang gelap
        topBar = {
            CustomTopAppBar( // Toolbar dengan tombol refresh
                title = DestinasiTerapis.titleRes,
                canNavigateBack = false,
                scrollBehavior = scrollBehavior,
                onRefresh = {
                    viewModel.getTer() // Memuat ulang data terapis
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToTerapisEntry,
                shape = CircleShape,
                modifier = Modifier.padding(18.dp),
                containerColor = Color(0xFF8B0000),
                contentColor = Color.White // Warna teks putih
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
            )
        }
    ) { innerPadding ->
        // Menampilkan status data terapis
        TerapisStatus(
            homeTUiState = viewModel.terUiState,
            retryAction = { viewModel.getTer() },
            modifier = Modifier.padding(innerPadding),
            onDetailTerapisClick = onDetailTerapisClick,
            onDeleteClick = { terapis ->
                viewModel.deleteTer(terapis.id_terapis) // Menghapus data terapis
                viewModel.getTer() // Memuat ulang data setelah dihapus
            },
            navigateToUpdateTerapis = navigateToUpdateTerapis
        )
    }
}

@Composable
fun TerapisStatus(
    homeTUiState: HomeTUiState, // Status data terapis
    retryAction: () -> Unit, // Aksi untuk memuat ulang
    navigateToUpdateTerapis: (String) -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: (Terapis) -> Unit = {},
    onDetailTerapisClick: (String) -> Unit
) {
    when (homeTUiState) {
        is HomeTUiState.Loading -> TerapisLoading(modifier = modifier.fillMaxSize()) // Menampilkan loading
        is HomeTUiState.Success -> {
            if (homeTUiState.terapis.isEmpty()) {
                // Tampilkan pesan jika data kosong
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Tidak ada data Terapis", color = Color.White)
                }
            } else {
                // Tampilkan daftar terapis
                TerapisLayout(
                    terapis = homeTUiState.terapis,
                    modifier = modifier.fillMaxWidth(),
                    navigateToUpdateTerapis = navigateToUpdateTerapis,
                    onDetailTerapisClick = { onDetailTerapisClick(it.id_terapis) },
                    onDeleteClick = { onDeleteClick(it) },
                )
            }
        }
        is HomeTUiState.Error -> TerapisError(retryAction, modifier = modifier.fillMaxSize()) // Tampilkan pesan error
    }
}

@Composable
fun TerapisLoading(
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
fun TerapisError(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp), color = Color.White)
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry), color = Color.White)
        }
    }
}

@Composable
fun TerapisLayout(
    terapis: List<Terapis>,
    modifier: Modifier = Modifier,
    navigateToUpdateTerapis: (String) -> Unit,
    onDetailTerapisClick: (Terapis) -> Unit,
    onDeleteClick: (Terapis) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(terapis) { terapis ->
            TerapisCard(
                terapis = terapis,
                modifier = Modifier
                    .fillMaxWidth(),
                navigateToUpdateTerapis = navigateToUpdateTerapis, // Fungsi klik untuk update
                onDeleteClick = { onDeleteClick(terapis) },
                onDetailTerapisClick = onDetailTerapisClick
            )
        }
    }
}

@Composable
fun TerapisCard(
    terapis: Terapis, // Data Terapis
    modifier: Modifier = Modifier,
    navigateToUpdateTerapis: (String) -> Unit,
    onDeleteClick: (Terapis) -> Unit = {},
    onDetailTerapisClick: (Terapis) -> Unit
) {
    var deleteConfirmationRequired by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .shadow(8.dp, shape = RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = Color.White) // Efek ripple putih
            ) { onDetailTerapisClick(terapis) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF8B0000), // Warna merah gelap
            contentColor = Color.White // Warna teks putih
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Gambar profil
            Image(
                painter = painterResource(id = R.drawable.terapis),
                contentDescription = "Foto Terapis",
                modifier = Modifier
                    .size(48.dp)
                    .clip(MaterialTheme.shapes.small) // Bentuk lingkaran
            )

            Spacer(Modifier.width(16.dp)) // Jarak antar elemen

            // Informasi Terapis
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = terapis.nama_terapis,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White // Warna teks putih
                )
                Text(
                    text = terapis.id_terapis,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f) // Warna teks putih dengan transparansi
                )
                Text(
                    text = terapis.spesialisasi,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f) // Warna teks putih dengan transparansi
                )
                Text(
                    text = terapis.nomor_izin_praktik,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f) // Warna teks putih dengan transparansi
                )
            }

            // Tombol Edit
            IconButton(
                onClick = { navigateToUpdateTerapis(terapis.id_terapis) },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Terapis",
                    tint = Color.White // Warna ikon putih
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
                    tint = Color.White // Warna ikon putih
                )
            }
        }
    }

    // Dialog konfirmasi hapus
    if (deleteConfirmationRequired) {
        DeleteConfirmationDialog(
            onDeleteConfirm = {
                deleteConfirmationRequired = false
                onDeleteClick(terapis)
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
        title = { Text("Delete Data", color = Color.White) },
        text = { Text("Apakah anda yakin ingin menghapus data ini?", color = Color.White) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = "Cancel", color = Color.White)
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = "Yes", color = Color.White)
            }
        },
        containerColor = Color(0xFF8B0000), // Warna merah gelap
        textContentColor = Color.White, // Warna teks putih
        titleContentColor = Color.White // Warna teks putih
    )
}