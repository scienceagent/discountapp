package com.example.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.CalculationRecord
import java.util.Locale

@Composable
fun ResultDisplayCard(record: CalculationRecord) {
  val formattedInitialPrice =
    String.format(Locale.US, "%.2f %s", record.initialPrice, record.currency)
  val formattedDiscount =
    String.format(Locale.US, "-%.2f %s", record.discountAmount, record.currency)
  val formattedFinalPrice =
    String.format(Locale.US, "%.2f %s", record.finalPrice, record.currency)
  val percentStr = String.format(Locale.US, "%.1f%%", record.discountPercent)

  ElevatedCard(
    shape = RoundedCornerShape(22.dp),
    colors =
      CardDefaults.elevatedCardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
      ),
    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
    modifier =
      Modifier.fillMaxWidth()
        .testTag("result_card"),
  ) {
    Column(
      modifier = Modifier.padding(20.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(22.dp),
          )
          Text(
            text = stringResource(R.string.summary_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
          )
        }

        // Savings badge
        Surface(
          shape = CircleShape,
          color = MaterialTheme.colorScheme.secondaryContainer,
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
          ) {
            Icon(
              imageVector = Icons.Default.Savings,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onSecondaryContainer,
              modifier = Modifier.size(16.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "-$percentStr",
              style = MaterialTheme.typography.labelLarge,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
          }
        }
      }

      HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

      // Breakdown rows
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Text(
          text = "Preț inițial:",
          style = MaterialTheme.typography.bodyLarge,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
          text = formattedInitialPrice,
          style = MaterialTheme.typography.bodyLarge,
          textDecoration = TextDecoration.LineThrough,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontWeight = FontWeight.Medium,
          modifier = Modifier.testTag("output_initial_price"),
        )
      }

      // Output 1: Valoarea reducerii
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          text = stringResource(R.string.output_discount_value) + ":",
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.SemiBold,
          color = MaterialTheme.colorScheme.secondary,
        )
        Text(
          text = formattedDiscount,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.secondary,
          modifier = Modifier.testTag("output_discount_value"),
        )
      }

      HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

      // Output 2: Prețul final
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Column {
          Text(
            text = stringResource(R.string.output_final_price) + ":",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
          )
          Text(
            text = "Total de achitat",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
        Text(
          text = formattedFinalPrice,
          style = MaterialTheme.typography.headlineMedium,
          fontWeight = FontWeight.ExtraBold,
          color = MaterialTheme.colorScheme.primary,
          modifier = Modifier.testTag("output_final_price"),
        )
      }
    }
  }
}

@Composable
fun CalculationHistorySection(
  history: List<CalculationRecord>,
  onClearHistory: () -> Unit,
  onSelectHistory: (CalculationRecord) -> Unit,
) {
  Column(
    modifier = Modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(10.dp),
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
      ) {
        Icon(
          imageVector = Icons.Default.History,
          contentDescription = null,
          modifier = Modifier.size(18.dp),
          tint = MaterialTheme.colorScheme.primary,
        )
        Text(
          text = "Istoric calcule recente",
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
        )
      }
      TextButton(onClick = onClearHistory) {
        Text("Curăță istoric")
      }
    }

    history.take(4).forEach { item ->
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
        modifier =
          Modifier.fillMaxWidth()
            .clickable { onSelectHistory(item) },
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Column {
            Text(
              text =
                String.format(
                  Locale.US,
                  "%.2f %s - %.0f%% reducere",
                  item.initialPrice,
                  item.currency,
                  item.discountPercent,
                ),
              style = MaterialTheme.typography.bodyMedium,
              fontWeight = FontWeight.Medium,
            )
            Text(
              text =
                String.format(
                  Locale.US,
                  "Economie: %.2f %s",
                  item.discountAmount,
                  item.currency,
                ),
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.secondary,
            )
          }

          Text(
            text = String.format(Locale.US, "%.2f %s", item.finalPrice, item.currency),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
          )
        }
      }
    }
  }
}

@Composable
fun LaboratorInfoDialog(onDismiss: () -> Unit) {
  AlertDialog(
    onDismissRequest = onDismiss,
    icon = { Icon(Icons.Default.Info, contentDescription = null) },
    title = {
      Text(
        text = "Laborator 1 - Specificații",
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
      )
    },
    text = {
      Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.verticalScroll(rememberScrollState()),
      ) {
        Text(
          text =
            "Tema: Introducerea în programarea cross platform utilizând Flutter / Jetpack Compose. Utilizarea de componente UI simple și evenimente asupra lor.",
          style = MaterialTheme.typography.bodyMedium,
        )
        Text(
          text = "Varianta 1: Calculator de reducere",
          fontWeight = FontWeight.Bold,
          style = MaterialTheme.typography.bodyMedium,
        )
        Text(
          text =
            "• Input: Preț inițial, Procentul reducerii (două TextField)\n" +
              "• UI Controls: TextField, DropdownButton / RadioButton, ElevatedButton\n" +
              "• Output: Valoarea reducerii și prețul final afișate în Text.",
          style = MaterialTheme.typography.bodySmall,
        )
      }
    },
    confirmButton = {
      TextButton(onClick = onDismiss) {
        Text("Închide")
      }
    },
  )
}

@Composable
fun FlutterCodeDialog(onDismiss: () -> Unit) {
  AlertDialog(
    onDismissRequest = onDismiss,
    icon = { Icon(Icons.Default.Code, contentDescription = null) },
    title = {
      Text(
        text = "Fișiere Laborator 1 (Flutter)",
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
      )
    },
    text = {
      Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.verticalScroll(rememberScrollState()),
      ) {
        Text(
          text = "Fișierul a fost generat în directorul proiectului:",
          style = MaterialTheme.typography.bodySmall,
          fontWeight = FontWeight.SemiBold,
        )
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = MaterialTheme.colorScheme.surfaceVariant,
          modifier = Modifier.fillMaxWidth(),
        ) {
          Text(
            text = "/laborator1_flutter/lib/main.dart",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(10.dp),
          )
        }
        Text(
          text =
            "Conține implementarea completă în Flutter cu componentele solicitate:\n" +
              "- 2x TextField (Preț inițial, Procent)\n" +
              "- DropdownButton pentru monedă\n" +
              "- RadioButton pentru selecție procent (10%, 20%, 30%, 50%, altul)\n" +
              "- ElevatedButton pentru calcul\n" +
              "- Text pentru valoarea reducerii și prețul final.",
          style = MaterialTheme.typography.bodySmall,
        )
      }
    },
    confirmButton = {
      TextButton(onClick = onDismiss) {
        Text("Înțeles")
      }
    },
  )
}
