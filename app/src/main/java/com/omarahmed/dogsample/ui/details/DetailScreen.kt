package com.omarahmed.dogsample.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.omarahmed.dogsample.R
import com.omarahmed.dogsample.ui.home.sampleDog
import com.omarahmed.dogsample.ui.theme.DogSampleTheme


@Composable
fun DetailsRoute(
    detailViewModel: DetailViewModel = hiltViewModel()
) {
    DetailScreen(detailViewModel.uiState.collectAsStateWithLifecycle())
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailScreen(
    uiState: State<DetailViewModel.DetailUiState>,
) {
    when (val state = uiState.value) {
        is DetailViewModel.DetailUiState.Success -> {
            Column {
                GlideImage(
                    modifier = Modifier
                        .height(300.dp)
                        .fillMaxWidth(),
                    model = state.dog.image,
                    loading = placeholder(R.drawable.placeholder_dog),
                    failure = placeholder(R.drawable.placeholder_dog),
                    contentDescription = "DogImage",
                    contentScale = ContentScale.Crop
                )
                val details = buildList {
                    add("Breed" to state.dog.breed.name)
                    add("Group" to state.dog.breed.group)
                    add("Life Span" to state.dog.breed.lifeSpan)
                    add("Temperament" to state.dog.breed.temperament)
                }
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(details) {
                        DetailItem(it)
                    }
                }
            }
        }

        is DetailViewModel.DetailUiState.Error -> Text(text = "Error")
        is DetailViewModel.DetailUiState.Loading -> Text(text = "Loading")
    }
}

@Composable
fun DetailItem(pair: Pair<String, String>) {
    Column {
        Text(
            text = pair.first,
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = pair.second,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    DogSampleTheme {
        DetailScreen(mutableStateOf(DetailViewModel.DetailUiState.Success(sampleDog)))
    }
}