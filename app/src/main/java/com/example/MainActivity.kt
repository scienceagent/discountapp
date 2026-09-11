package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CalculationRecord
import com.example.ui.CalculationHistorySection
import com.example.ui.FlutterCodeDialog
import com.example.ui.LaboratorInfoDialog
import com.example.ui.ResultDisplayCard
import com.example.ui.theme.MyApplicationTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        DiscountCalculatorApp()
      }
    }
  }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(text = "Hello $name!", modifier = modifier)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscountCalculatorApp() {
  var showInfoDialog by remember { mutableStateOf(false) }
  var showFlutterCodeDialog by remember { mutableStateOf(false) }

  Scaffold(
    topBar = {
      CenterAlignedTopAppBar(
        title = {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
          ) {
            Box(
              modifier =
                Modifier.size(32.dp)
                  .clip(CircleShape)
                  .background(MaterialTheme.colorScheme.primaryContainer),
              contentAlignment = Alignment.Center,
            ) {
              Icon(
                imageVector = Icons.Default.LocalOffer,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(18.dp),
              )
            }
            Text(
              text = stringResource(R.string.app_name),
              fontWeight = FontWeight.Bold,
              style = MaterialTheme.typography.titleMedium,
            )
          }
        },
        actions = {
          IconButton(
            onClick = { showFlutterCodeDialog = true },
            modifier = Modifier.testTag("code_button"),
          ) {
            Icon(
              imageVector = Icons.Default.Code,
              contentDescription = "Cod Flutter Laborator",
              tint = MaterialTheme.colorScheme.primary,
            )
          }
          IconButton(
            onClick = { showInfoDialog = true },
            modifier = Modifier.testTag("info_button"),
          ) {
            Icon(
              imageVector = Icons.Default.Info,
              contentDescription = "Informații Laborator 1",
            )
          }
        },
        colors =
          TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
          ),
      )
    },
    containerColor = MaterialTheme.colorScheme.background,
  ) { innerPadding ->
    Box(
      modifier =
        Modifier.fillMaxSize()
          .padding(innerPadding),
      contentAlignment = Alignment.TopCenter,
    ) {
      DiscountCalculatorContent(
        modifier =
          Modifier.widthIn(max = 600.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
      )
    }

    if (showInfoDialog) {
      LaboratorInfoDialog(onDismiss = { showInfoDialog = false })
    }

    if (showFlutterCodeDialog) {
      FlutterCodeDialog(onDismiss = { showFlutterCodeDialog = false })
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DiscountCalculatorContent(modifier: Modifier = Modifier) {
  val focusManager = LocalFocusManager.current
  val scrollState = rememberScrollState()

  // Input states: 2 TextFields (Preț inițial & Procentul reducerii)
  var initialPriceText by remember { mutableStateOf("") }
  var discountPercentText by remember { mutableStateOf("10") }
  var selectedRadioPreset by remember { mutableStateOf("10") }
  val presets = listOf("5", "10", "15", "20", "25", "50", "custom")

  // Currency DropdownButton state (ExposedDropdownMenuBox)
  val currencies = listOf("MDL (lei)", "RON (lei)", "EUR (€)", "USD ($)")
  var selectedCurrency by remember { mutableStateOf(currencies[0]) }
  var currencyDropdownExpanded by remember { mutableStateOf(false) }

  // Calculation state & validation
  var calculatedRecord by remember { mutableStateOf<CalculationRecord?>(null) }
  var priceError by remember { mutableStateOf<String?>(null) }
  var discountError by remember { mutableStateOf<String?>(null) }

  // History list
  val historyList = remember { mutableStateListOf<CalculationRecord>() }

  fun performCalculation() {
    val cleanPrice = initialPriceText.replace(',', '.').trim()
    val cleanDiscount = discountPercentText.replace(',', '.').trim()

    val price = cleanPrice.toDoubleOrNull()
    val discount = cleanDiscount.toDoubleOrNull()

    var hasError = false

    if (price == null || price <= 0.0) {
      priceError = "Introduceți un preț valid (> 0)"
      hasError = true
    } else {
      priceError = null
    }

    if (discount == null || discount < 0.0 || discount > 100.0) {
      discountError = "Procentul trebuie să fie între 0% și 100%"
      hasError = true
    } else {
      discountError = null
    }

    if (!hasError && price != null && discount != null) {
      val discountVal = (price * discount) / 100.0
      val finalPriceVal = price - discountVal
      val currencySymbol = selectedCurrency.substringBefore(" ").trim()
      val record =
        CalculationRecord(
          initialPrice = price,
          discountPercent = discount,
          discountAmount = discountVal,
          finalPrice = finalPriceVal,
          currency = currencySymbol,
        )
      calculatedRecord = record
      historyList.add(0, record)
      if (historyList.size > 10) {
        historyList.removeLast()
      }
      focusManager.clearFocus()
    } else {
      calculatedRecord = null
    }
  }

  fun resetAll() {
    initialPriceText = ""
    discountPercentText = "10"
    selectedRadioPreset = "10"
    priceError = null
    discountError = null
    calculatedRecord = null
    focusManager.clearFocus()
  }

  Column(
    modifier = modifier.verticalScroll(scrollState),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    // Header card
    Card(
      colors =
        CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
        ),
      shape = RoundedCornerShape(20.dp),
      modifier = Modifier.fillMaxWidth(),
    ) {
      Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Box(
          modifier =
            Modifier.size(44.dp)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.primary),
          contentAlignment = Alignment.Center,
        ) {
          Icon(
            imageVector = Icons.Default.Percent,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(24.dp),
          )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = stringResource(R.string.lab_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
          )
          Text(
            text = stringResource(R.string.lab_variant),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
          )
        }
      }
    }

    // Input Section Card
    ElevatedCard(
      shape = RoundedCornerShape(20.dp),
      colors =
        CardDefaults.elevatedCardColors(
          containerColor = MaterialTheme.colorScheme.surface,
        ),
      elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
      modifier = Modifier.fillMaxWidth(),
    ) {
      Column(
        modifier = Modifier.padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
      ) {
        Text(
          text = "Parametrii calculului",
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary,
        )

        // UI Control: DropdownButton (ExposedDropdownMenuBox)
        ExposedDropdownMenuBox(
          expanded = currencyDropdownExpanded,
          onExpandedChange = { currencyDropdownExpanded = !currencyDropdownExpanded },
          modifier = Modifier.fillMaxWidth(),
        ) {
          OutlinedTextField(
            value = selectedCurrency,
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.currency_label)) },
            trailingIcon = {
              ExposedDropdownMenuDefaults.TrailingIcon(expanded = currencyDropdownExpanded)
            },
            leadingIcon = {
              Icon(
                imageVector = Icons.Default.LocalOffer,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
              )
            },
            shape = RoundedCornerShape(14.dp),
            modifier =
              Modifier.fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                .testTag("currency_dropdown"),
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
          )

          ExposedDropdownMenu(
            expanded = currencyDropdownExpanded,
            onDismissRequest = { currencyDropdownExpanded = false },
          ) {
            currencies.forEach { currency ->
              DropdownMenuItem(
                text = { Text(currency) },
                onClick = {
                  selectedCurrency = currency
                  currencyDropdownExpanded = false
                  if (calculatedRecord != null) {
                    performCalculation()
                  }
                },
                modifier = Modifier.testTag("currency_item_$currency"),
              )
            }
          }
        }

        // 1. TextField: Preț inițial
        OutlinedTextField(
          value = initialPriceText,
          onValueChange = {
            initialPriceText = it
            if (priceError != null) priceError = null
          },
          label = { Text(stringResource(R.string.input_initial_price)) },
          placeholder = { Text("ex: 299.99") },
          leadingIcon = {
            Text(
              text = selectedCurrency.substringBefore(" "),
              fontWeight = FontWeight.Bold,
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.primary,
              modifier = Modifier.padding(start = 12.dp),
            )
          },
          trailingIcon = {
            if (initialPriceText.isNotEmpty()) {
              IconButton(onClick = { initialPriceText = "" }) {
                Icon(Icons.Default.Clear, contentDescription = "Șterge preț")
              }
            }
          },
          isError = priceError != null,
          supportingText = {
            if (priceError != null) {
              Text(text = priceError ?: "", color = MaterialTheme.colorScheme.error)
            } else {
              Text("Introduceți prețul înainte de reducere")
            }
          },
          keyboardOptions =
            KeyboardOptions(
              keyboardType = KeyboardType.Decimal,
              imeAction = ImeAction.Next,
            ),
          singleLine = true,
          shape = RoundedCornerShape(14.dp),
          modifier =
            Modifier.fillMaxWidth()
              .testTag("initial_price_input"),
        )

        // UI Control: RadioButton - Reduceri rapide
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
          Text(
            text = "Alegeți procentul reducerii (RadioButton):",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
          )

          FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth(),
          ) {
            presets.forEach { preset ->
              val isSelected = selectedRadioPreset == preset
              val label = if (preset == "custom") "Altul" else "$preset%"

              Surface(
                shape = RoundedCornerShape(12.dp),
                color =
                  if (isSelected) {
                    MaterialTheme.colorScheme.primaryContainer
                  } else {
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                  },
                border =
                  if (isSelected) {
                    androidx.compose.foundation.BorderStroke(
                      1.5.dp,
                      MaterialTheme.colorScheme.primary,
                    )
                  } else {
                    null
                  },
                modifier =
                  Modifier.clip(RoundedCornerShape(12.dp))
                    .clickable(
                      role = Role.RadioButton,
                      onClick = {
                        selectedRadioPreset = preset
                        if (preset != "custom") {
                          discountPercentText = preset
                          discountError = null
                          if (initialPriceText.isNotEmpty()) {
                            performCalculation()
                          }
                        }
                      },
                    )
                    .testTag("radio_preset_$preset"),
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                ) {
                  RadioButton(
                    selected = isSelected,
                    onClick = null,
                    colors =
                      RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.primary,
                      ),
                    modifier = Modifier.size(20.dp),
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color =
                      if (isSelected) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                      } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                      },
                  )
                }
              }
            }
          }
        }

        // 2. TextField: Procentul reducerii
        OutlinedTextField(
          value = discountPercentText,
          onValueChange = {
            discountPercentText = it
            if (discountError != null) discountError = null
            if (selectedRadioPreset != "custom" && it != selectedRadioPreset) {
              selectedRadioPreset = "custom"
            }
          },
          label = { Text(stringResource(R.string.input_discount_percent)) },
          placeholder = { Text("ex: 15") },
          leadingIcon = {
            Icon(
              imageVector = Icons.Default.Percent,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
            )
          },
          trailingIcon = {
            Text(
              text = "%",
              fontWeight = FontWeight.Bold,
              style = MaterialTheme.typography.bodyLarge,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.padding(end = 16.dp),
            )
          },
          isError = discountError != null,
          supportingText = {
            if (discountError != null) {
              Text(text = discountError ?: "", color = MaterialTheme.colorScheme.error)
            } else {
              Text("Valoare între 0% și 100%")
            }
          },
          keyboardOptions =
            KeyboardOptions(
              keyboardType = KeyboardType.Decimal,
              imeAction = ImeAction.Done,
            ),
          keyboardActions =
            KeyboardActions(
              onDone = {
                performCalculation()
              },
            ),
          singleLine = true,
          shape = RoundedCornerShape(14.dp),
          modifier =
            Modifier.fillMaxWidth()
              .testTag("discount_percent_input"),
        )

        // UI Control: ElevatedButton & Reset button
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          ElevatedButton(
            onClick = { performCalculation() },
            shape = RoundedCornerShape(14.dp),
            colors =
              ButtonDefaults.elevatedButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
              ),
            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 3.dp),
            modifier =
              Modifier.weight(1f)
                .height(52.dp)
                .testTag("calculate_button"),
          ) {
            Icon(
              imageVector = Icons.Default.Calculate,
              contentDescription = null,
              modifier = Modifier.size(20.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = stringResource(R.string.btn_calculate),
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp,
            )
          }

          OutlinedButton(
            onClick = { resetAll() },
            shape = RoundedCornerShape(14.dp),
            modifier =
              Modifier.height(52.dp)
                .testTag("reset_button"),
          ) {
            Icon(
              imageVector = Icons.Default.Refresh,
              contentDescription = "Reset",
              modifier = Modifier.size(20.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(stringResource(R.string.btn_reset))
          }
        }
      }
    }

    // Output: Valoarea reducerii și prețul final afișate în Text
    AnimatedVisibility(
      visible = calculatedRecord != null,
      enter = fadeIn() + slideInVertically(),
    ) {
      calculatedRecord?.let { record ->
        ResultDisplayCard(record = record)
      }
    }

    // Calculation History
    if (historyList.isNotEmpty()) {
      CalculationHistorySection(
        history = historyList,
        onClearHistory = { historyList.clear() },
        onSelectHistory = { rec ->
          initialPriceText = String.format(Locale.US, "%.2f", rec.initialPrice)
          discountPercentText = String.format(Locale.US, "%.0f", rec.discountPercent)
          selectedRadioPreset =
            if (presets.contains(discountPercentText)) discountPercentText else "custom"
          calculatedRecord = rec
        },
      )
    }

    Spacer(modifier = Modifier.height(24.dp))
  }
}

@Preview(showBackground = true)
@Composable
fun DiscountCalculatorPreview() {
  MyApplicationTheme {
    DiscountCalculatorApp()
  }
}
