package com.omarahmed.dogsample.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.omarahmed.dogsample.R
import com.omarahmed.dogsample.model.Breed
import com.omarahmed.dogsample.model.Dog
import com.omarahmed.dogsample.model.ListType
import com.omarahmed.dogsample.ui.theme.DogSampleTheme


@Composable
fun HomeRoute(
    viewModel: DogsViewModel = hiltViewModel(),
    navToDetails: (Dog) -> Unit
) {
    HomeScreen(
        viewModel.uiState.collectAsStateWithLifecycle(),
        viewModel.listType.collectAsStateWithLifecycle(),
        onRetry = { viewModel.getAll() },
        onDogClicked = { dog -> navToDetails(dog) }
    ) { viewModel.changeListType() }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    uiState: State<DogsViewModel.UiState>,
    listType: State<ListType>,
    onRetry: () -> Unit,
    onDogClicked: (Dog) -> Unit,
    onChangeListType: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Compose DogSample") },
                actions = {
                    IconButton(onClick = { onChangeListType() }) {
                        val iconRes = when (listType.value) {
                            ListType.LIST -> R.drawable.ic_grid_view
                            ListType.GIRD -> R.drawable.ic_list_view
                        }
                        Icon(
                            imageVector = ImageVector.vectorResource(id = iconRes),
                            contentDescription = "Localized description"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (val state = uiState.value) {
                is DogsViewModel.UiState.Success -> {
                    val count = when (listType.value) {
                        ListType.LIST -> 1
                        ListType.GIRD -> 2
                    }
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(count),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(state.dogs) {
                            DogItem(
                                Modifier.animateItemPlacement(),
                                it,
                                listType
                            ) { dogClicked -> onDogClicked(dogClicked) }
                        }
                    }
                }

                is DogsViewModel.UiState.Error -> {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(state.exception.localizedMessage)
                        Button(onClick = { onRetry() }) {
                            Text("Retry")
                        }
                    }
                }

                is DogsViewModel.UiState.Loading -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }


}


@Composable
fun DogItem(
    modifier: Modifier = Modifier,
    dog: Dog,
    listType: State<ListType>,
    onClicked: (Dog) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClicked(dog) }
    ) {
        when (listType.value) {
            ListType.LIST -> DogItemList(dog)
            ListType.GIRD -> DogItemGrid(dog)
        }

    }
}

@Composable
@OptIn(ExperimentalGlideComposeApi::class)
private fun DogItemGrid(dog: Dog) {
    GlideImage(
        modifier = Modifier
            .height(160.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
        model = dog.image,
        loading = placeholder(R.drawable.placeholder_dog),
        failure = placeholder(R.drawable.placeholder_dog),
        contentDescription = "DogImage",
        contentScale = ContentScale.Crop
    )
}

@Composable
@OptIn(ExperimentalGlideComposeApi::class)
private fun DogItemList(dog: Dog) {
    Row(modifier = Modifier.padding(12.dp)) {
        GlideImage(
            modifier = Modifier
                .size(80.dp, 80.dp)
                .clip(RoundedCornerShape(8.dp)),
            model = dog.image,
            loading = placeholder(R.drawable.placeholder_dog),
            failure = placeholder(R.drawable.placeholder_dog),
            contentDescription = "DogImage",
            contentScale = ContentScale.Crop
        )
        Text(
            modifier = Modifier
                .padding(start = 16.dp)
                .align(Alignment.CenterVertically),
            text = dog.breed.name,
            style = MaterialTheme.typography.titleMedium
        )
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    DogSampleTheme {
        HomeScreen(
            mutableStateOf(DogsViewModel.UiState.Success(sampleDogs)),
            mutableStateOf(ListType.GIRD),
            {}, {}, {})
    }
}


val sampleDog = Dog(
    "id",
    "https://images.unsplash.com/photo-1598133894008-61f7fdb8cc3a?q=80&w=1976&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
    Breed("id", "name", "group", "lifespan", "ter")
)
val sampleDogs = listOf(sampleDog, sampleDog, sampleDog)