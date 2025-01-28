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
    navigateToPasienEntry: () -> Unit,
    navigateToPasien: () -> Unit,
    navigateToTerapis: () -> Unit,
    navigateToJenisTerapi: () -> Unit,
    navigateToSesiTerapi: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit = {},
    navigateToUpdatePasien: (String) -> Unit,
    viewModel: HomePasienViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(Color(0xFF121212)), // Warna latar belakang gelap
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
                shape = CircleShape,
                modifier = Modifier.padding(18.dp),
                containerColor = Color(0xFF8B0000),
                contentColor = Color.White // Warna teks putih
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
            )
        }
    ) { innerPadding ->
        HomeStatus(
            homeUiState = viewModel.pasUiState,
            retryAction = { viewModel.getPas() },
            modifier = Modifier.padding(innerPadding),
            onDetailClick = { id_pasien ->
                onDetailClick(id_pasien)
            },
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
        is HomeUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())
        is HomeUiState.Success -> {
            if (homeUiState.pasien.isEmpty()) {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Tidak ada data Pasien", style = MaterialTheme.typography.titleMedium)
                }
            } else {
                PasLayout(
                    pasien = homeUiState.pasien,
                    modifier = modifier.fillMaxWidth(),
                    navigateToUpdatePasien = navigateToUpdatePasien,
                    onDetailClick = { onDetailClick(it.id_pasien) },
                    onDeleteClick = { onDeleteClick(it) }
                )
            }
        }
        is HomeUiState.Error -> OnError(retryAction, modifier = modifier.fillMaxSize())
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
                    .fillMaxWidth(),
                navigateToUpdatePasien = navigateToUpdatePasien,
                onDeleteClick = { onDeleteClick(pasien) },
                onDetailClick = onDetailClick
            )
        }
    }
}

@Composable
fun PasCard(
    pasien: Pasien,
    modifier: Modifier = Modifier,
    navigateToUpdatePasien: (String) -> Unit,
    onDeleteClick: (Pasien) -> Unit = {},
    onDetailClick: (Pasien) -> Unit
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
            ) { onDetailClick(pasien)  },
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
                painter = painterResource(id = R.drawable.pasien),
                contentDescription = "Foto Pasien",
                modifier = Modifier
                    .size(48.dp)
                    .clip(MaterialTheme.shapes.small) // Bentuk lingkaran
            )
            Spacer(Modifier.width(16.dp)) // Jarak antar elemen

            // Informasi Pasien
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = pasien.nama_pasien,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White // Warna teks putih
                )
                Text(
                    text = pasien.id_pasien,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f) // Warna teks putih dengan transparansi
                )
                Text(
                    text = pasien.alamat,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f) // Warna teks putih dengan transparansi
                )
            }

            // Tombol Edit
            IconButton(
                onClick = { navigateToUpdatePasien(pasien.id_pasien) },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Pasien",
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