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
import com.dwan.ta_pam.model.SesiTerapi
import com.dwan.ta_pam.ui.customwidget.CustomTopAppBar
import com.dwan.ta_pam.ui.customwidget.MenuButton
import com.dwan.ta_pam.ui.navigation.DestinasiNavigasi
import com.dwan.ta_pam.ui.viewmodel.HomeSUiState
import com.dwan.ta_pam.ui.viewmodel.PenyediaViewModel
import com.dwan.ta_pam.ui.viewmodel.SesiTerapiViewModel

// Objek untuk mendefinisikan rute dan judul layar home
object DestinasiSesiTerapi : DestinasiNavigasi {
    override val route = "sesi_terapi" // Rute navigasi layar home
    override val titleRes = "Home Sesi Terapi"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SesiTerapiScreen(
    navigateToSesiTerapiEntry: () -> Unit,
    navigateToPasien: () -> Unit,
    navigateToTerapis: () -> Unit,
    navigateToJenisTerapi: () -> Unit,
    navigateToSesiTerapi: () -> Unit,
    modifier: Modifier = Modifier,
    navigateToUpdateSesiTerapi: (String) -> Unit,
    onDetailSesiTerapiClick: (String) -> Unit,
    viewModel: SesiTerapiViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(Color(0xFF121212)), // Warna latar belakang gelap
        topBar = {
            CustomTopAppBar(
                title = DestinasiSesiTerapi.titleRes,
                canNavigateBack = false,
                scrollBehavior = scrollBehavior,
                onRefresh = {
                    viewModel.getSesi()
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToSesiTerapiEntry,
                shape = CircleShape,
                modifier = Modifier.padding(18.dp),
                containerColor = Color(0xFF8B0000),
                contentColor = Color.White // Warna teks putih
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Sesi Terapi")
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
        SesiTerapiStatus(
            homeSUiState = viewModel.sesiUiState,
            retryAction = { viewModel.getSesi() },
            modifier = Modifier.padding(innerPadding),
            onDetailSesiTerapiClick = onDetailSesiTerapiClick,
            onDeleteClick = { sesi_terapi ->
                viewModel.deleteSesi(sesi_terapi.id_sesi)
                viewModel.getSesi()
            },
            navigateToUpdateSesiTerapi = navigateToUpdateSesiTerapi
        )
    }
}

@Composable
fun SesiTerapiStatus(
    homeSUiState: HomeSUiState,
    retryAction: () -> Unit,
    navigateToUpdateSesiTerapi: (String) -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: (SesiTerapi) -> Unit = {},
    onDetailSesiTerapiClick: (String) -> Unit
) {
    when (homeSUiState) {
        is HomeSUiState.Loading -> SesiTerapiLoading(modifier = modifier.fillMaxSize())
        is HomeSUiState.Success -> {
            if (homeSUiState.sesiTerapi.isEmpty()) {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Tidak ada data Sesi Terapi", color = Color.White)
                }
            } else {
                SesiTerapiLayout(
                    sesiTerapi = homeSUiState.sesiTerapi,
                    modifier = modifier.fillMaxWidth(),
                    navigateToUpdateSesiTerapi = navigateToUpdateSesiTerapi,
                    onDeleteClick = { onDeleteClick(it) },
                    onDetailSesiTerapiClick = { onDetailSesiTerapiClick(it.id_sesi) }
                )
            }
        }
        is HomeSUiState.Error -> SesiTerapiError(retryAction, modifier = modifier.fillMaxSize())
    }
}

@Composable
fun SesiTerapiLayout(
    sesiTerapi: List<SesiTerapi>,
    modifier: Modifier = Modifier,
    navigateToUpdateSesiTerapi: (String) -> Unit,
    onDetailSesiTerapiClick: (SesiTerapi) -> Unit,
    onDeleteClick: (SesiTerapi) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(sesiTerapi) { sesiTerapi ->
            SesiTerapiCard(
                sesiTerapi = sesiTerapi,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDetailSesiTerapiClick(sesiTerapi) },
                navigateToUpdateSesiTerapi = navigateToUpdateSesiTerapi,
                onDeleteClick = { onDeleteClick(sesiTerapi) },
                onDetailSesiTerapiClick = onDetailSesiTerapiClick
            )
        }
    }
}

@Composable
fun SesiTerapiCard(
    sesiTerapi: SesiTerapi,
    modifier: Modifier = Modifier,
    navigateToUpdateSesiTerapi: (String) -> Unit,
    onDeleteClick: (SesiTerapi) -> Unit = {},
    onDetailSesiTerapiClick: (SesiTerapi) -> Unit
) {
    var deleteConfirmationRequired by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .shadow(8.dp, shape = RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = Color.White),
                onClick = { onDetailSesiTerapiClick(sesiTerapi) }
            ),
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
            Image(
                painter = painterResource(id = R.drawable.sesi),
                contentDescription = "Foto Sesi Terapi",
                modifier = Modifier
                    .size(48.dp)
                    .clip(MaterialTheme.shapes.small)
            )

            Spacer(Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = sesiTerapi.tanggal_sesi,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
                Text(
                    text = sesiTerapi.id_sesi,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }

            IconButton(
                onClick = { navigateToUpdateSesiTerapi(sesiTerapi.id_sesi) },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Sesi Terapi",
                    tint = Color.White
                )
            }

            IconButton(
                onClick = { deleteConfirmationRequired = true },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }

    if (deleteConfirmationRequired) {
        DeleteConfirmationDialog(
            onDeleteConfirm = {
                deleteConfirmationRequired = false
                onDeleteClick(sesiTerapi)
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
fun SesiTerapiLoading(
    modifier: Modifier
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.size(70.dp),
            painter = painterResource(id = R.drawable.loading_img),
            contentDescription = stringResource(R.string.loading)
        )
    }
}

@Composable
fun SesiTerapiError(retryAction: () -> Unit, modifier: Modifier = Modifier) {
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