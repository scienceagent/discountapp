import 'package:flutter/material.dart';

void main() {
  runApp(const CalculatorReducereApp());
}

class CalculatorReducereApp extends StatelessWidget {
  const CalculatorReducereApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Calculator Reducere - Lab 1',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(
          seedColor: const Color(0xFF0B57D0),
          brightness: Brightness.light,
        ),
        useMaterial3: true,
      ),
      darkTheme: ThemeData(
        colorScheme: ColorScheme.fromSeed(
          seedColor: const Color(0xFF0B57D0),
          brightness: Brightness.dark,
        ),
        useMaterial3: true,
      ),
      themeMode: ThemeMode.system,
      home: const DiscountCalculatorScreen(),
    );
  }
}

class DiscountCalculatorScreen extends StatefulWidget {
  const DiscountCalculatorScreen({super.key});

  @override
  State<DiscountCalculatorScreen> createState() =>
      _DiscountCalculatorScreenState();
}

class _DiscountCalculatorScreenState extends State<DiscountCalculatorScreen> {
  // Controllere pentru cele 2 TextField cerute:
  final TextEditingController _priceController = TextEditingController();
  final TextEditingController _discountController = TextEditingController();

  // Variabile de stare pentru RadioButton și DropdownButton
  String _selectedRadioDiscount = '10'; // 10%, 20%, 30%, 50%, custom
  String _selectedCurrency = 'MDL (lei)';
  final List<String> _currencies = ['MDL (lei)', 'RON (lei)', 'EUR (€)', 'USD ($)'];

  // Rezultate pentru afișare în Text
  double _initialPrice = 0.0;
  double _discountAmount = 0.0;
  double _finalPrice = 0.0;
  bool _hasCalculated = false;
  String? _errorMessage;

  @override
  void initState() {
    super.initState();
    _discountController.text = '10';
  }

  @override
  void dispose() {
    _priceController.dispose();
    _discountController.dispose();
    super.dispose();
  }

  // Funcția de calcul apelată la apăsarea ElevatedButton
  void _calculateDiscount() {
    setState(() {
      _errorMessage = null;
      final double? price = double.tryParse(_priceController.text.replaceAll(',', '.'));
      final double? discount = double.tryParse(_discountController.text.replaceAll(',', '.'));

      if (price == null || price <= 0) {
        _errorMessage = 'Vă rugăm să introduceți un preț inițial valid!';
        _hasCalculated = false;
        return;
      }

      if (discount == null || discount < 0 || discount > 100) {
        _errorMessage = 'Procentul de reducere trebuie să fie între 0% și 100%!';
        _hasCalculated = false;
        return;
      }

      _initialPrice = price;
      _discountAmount = (price * discount) / 100.0;
      _finalPrice = price - _discountAmount;
      _hasCalculated = true;
    });
  }

  void _resetFields() {
    setState(() {
      _priceController.clear();
      _discountController.text = '10';
      _selectedRadioDiscount = '10';
      _initialPrice = 0.0;
      _discountAmount = 0.0;
      _finalPrice = 0.0;
      _hasCalculated = false;
      _errorMessage = null;
    });
  }

  void _onRadioChanged(String? value) {
    if (value != null) {
      setState(() {
        _selectedRadioDiscount = value;
        if (value != 'custom') {
          _discountController.text = value;
          if (_priceController.text.isNotEmpty) {
            _calculateDiscount();
          }
        }
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final colorScheme = theme.colorScheme;

    return Scaffold(
      appBar: AppBar(
        title: const Text('Calculator de reducere'),
        centerTitle: true,
        backgroundColor: colorScheme.surfaceVariant,
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(20.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            // Header informativ Laborator 1
            Card(
              elevation: 0,
              color: colorScheme.primaryContainer.withOpacity(0.5),
              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: Row(
                  children: [
                    Icon(Icons.percent_rounded, color: colorScheme.primary, size: 36),
                    const SizedBox(width: 14),
                    Expanded(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(
                            'Laborator 1 - Varianta 1',
                            style: theme.textTheme.titleMedium?.copyWith(
                              fontWeight: FontWeight.bold,
                              color: colorScheme.onPrimaryContainer,
                            ),
                          ),
                          const SizedBox(height: 4),
                          Text(
                            'Calculați prețul final al unui produs după aplicarea unei reduceri.',
                            style: theme.textTheme.bodySmall?.copyWith(
                              color: colorScheme.onPrimaryContainer,
                            ),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 20),

            // DropdownButton pentru monedă
            Row(
              children: [
                const Icon(Icons.monetization_on_outlined),
                const SizedBox(width: 10),
                const Text('Monedă selectată: '),
                const Spacer(),
                DropdownButton<String>(
                  value: _selectedCurrency,
                  underline: Container(height: 2, color: colorScheme.primary),
                  items: _currencies.map((String value) {
                    return DropdownMenuItem<String>(
                      value: value,
                      child: Text(value),
                    );
                  }).toList(),
                  onChanged: (String? newValue) {
                    if (newValue != null) {
                      setState(() {
                        _selectedCurrency = newValue;
                      });
                    }
                  },
                ),
              ],
            ),
            const SizedBox(height: 16),

            // 1. TextField: Preț inițial
            TextField(
              controller: _priceController,
              keyboardType: const TextInputType.numberWithOptions(decimal: true),
              decoration: InputDecoration(
                labelText: 'Preț inițial',
                hintText: 'ex: 250.00',
                prefixIcon: const Icon(Icons.attach_money),
                border: const OutlineInputBorder(
                  borderRadius: BorderRadius.all(Radius.circular(12)),
                ),
                suffixText: _selectedCurrency.split(' ').first,
              ),
            ),
            const SizedBox(height: 16),

            // RadioButton pentru reduceri predefinite
            const Text(
              'Alegeți procentul reducerii (RadioButton):',
              style: TextStyle(fontWeight: FontWeight.w600),
            ),
            Wrap(
              spacing: 8,
              children: [
                Row(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Radio<String>(
                      value: '10',
                      groupValue: _selectedRadioDiscount,
                      onChanged: _onRadioChanged,
                    ),
                    const Text('10%'),
                  ],
                ),
                Row(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Radio<String>(
                      value: '20',
                      groupValue: _selectedRadioDiscount,
                      onChanged: _onRadioChanged,
                    ),
                    const Text('20%'),
                  ],
                ),
                Row(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Radio<String>(
                      value: '30',
                      groupValue: _selectedRadioDiscount,
                      onChanged: _onRadioChanged,
                    ),
                    const Text('30%'),
                  ],
                ),
                Row(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Radio<String>(
                      value: '50',
                      groupValue: _selectedRadioDiscount,
                      onChanged: _onRadioChanged,
                    ),
                    const Text('50%'),
                  ],
                ),
                Row(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Radio<String>(
                      value: 'custom',
                      groupValue: _selectedRadioDiscount,
                      onChanged: _onRadioChanged,
                    ),
                    const Text('Altul'),
                  ],
                ),
              ],
            ),
            const SizedBox(height: 8),

            // 2. TextField: Procentul reducerii
            TextField(
              controller: _discountController,
              keyboardType: const TextInputType.numberWithOptions(decimal: true),
              decoration: const InputDecoration(
                labelText: 'Procentul reducerii',
                hintText: 'ex: 15',
                prefixIcon: Icon(Icons.discount_outlined),
                suffixText: '%',
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.all(Radius.circular(12)),
                ),
              ),
              onChanged: (val) {
                if (_selectedRadioDiscount != 'custom' &&
                    val != _selectedRadioDiscount) {
                  setState(() {
                    _selectedRadioDiscount = 'custom';
                  });
                }
              },
            ),
            const SizedBox(height: 16),

            if (_errorMessage != null)
              Container(
                padding: const EdgeInsets.all(12),
                decoration: BoxDecoration(
                  color: colorScheme.errorContainer,
                  borderRadius: BorderRadius.circular(8),
                ),
                child: Text(
                  _errorMessage!,
                  style: TextStyle(color: colorScheme.onErrorContainer),
                ),
              ),
            const SizedBox(height: 16),

            // UI Control: ElevatedButton
            Row(
              children: [
                Expanded(
                  child: ElevatedButton.icon(
                    onPressed: _calculateDiscount,
                    icon: const Icon(Icons.calculate),
                    label: const Text('Calculează'),
                    style: ElevatedButton.styleFrom(
                      padding: const EdgeInsets.symmetric(vertical: 14),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(12),
                      ),
                    ),
                  ),
                ),
                const SizedBox(width: 12),
                OutlinedButton(
                  onPressed: _resetFields,
                  style: OutlinedButton.styleFrom(
                    padding: const EdgeInsets.symmetric(vertical: 14, horizontal: 16),
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(12),
                    ),
                  ),
                  child: const Text('Resetează'),
                ),
              ],
            ),
            const SizedBox(height: 24),

            // Output: valoarea reducerii și prețul final afișate în Text
            if (_hasCalculated) ...[
              Card(
                elevation: 2,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(16),
                ),
                child: Padding(
                  padding: const EdgeInsets.all(20.0),
                  child: Column(
                    children: [
                      Text(
                        'Rezultat calcul',
                        style: theme.textTheme.titleMedium?.copyWith(
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                      const Divider(height: 24),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          const Text('Preț inițial:', style: TextStyle(fontSize: 16)),
                          Text(
                            '${_initialPrice.toStringAsFixed(2)} $_selectedCurrency',
                            style: const TextStyle(fontSize: 16),
                          ),
                        ],
                      ),
                      const SizedBox(height: 10),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          const Text(
                            'Valoarea reducerii:',
                            style: TextStyle(
                              fontSize: 16,
                              fontWeight: FontWeight.w600,
                              color: Colors.green,
                            ),
                          ),
                          Text(
                            '-${_discountAmount.toStringAsFixed(2)} $_selectedCurrency',
                            style: const TextStyle(
                              fontSize: 18,
                              fontWeight: FontWeight.bold,
                              color: Colors.green,
                            ),
                          ),
                        ],
                      ),
                      const SizedBox(height: 10),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          const Text(
                            'Prețul final:',
                            style: TextStyle(
                              fontSize: 18,
                              fontWeight: FontWeight.bold,
                            ),
                          ),
                          Text(
                            '${_finalPrice.toStringAsFixed(2)} $_selectedCurrency',
                            style: TextStyle(
                              fontSize: 22,
                              fontWeight: FontWeight.bold,
                              color: colorScheme.primary,
                            ),
                          ),
                        ],
                      ),
                    ],
                  ),
                ),
              ),
            ],
          ],
        ),
      ),
    );
  }
}
