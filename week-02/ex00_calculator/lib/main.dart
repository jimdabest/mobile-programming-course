import 'package:flutter/material.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return const MaterialApp(
      debugShowCheckedModeBanner: false,
      home: CalculatorScreen(),
    );
  }
}

class CalculatorScreen extends StatefulWidget {
  const CalculatorScreen({super.key});

  @override
  State<CalculatorScreen> createState() => _CalculatorScreenState();
}

class _CalculatorScreenState extends State<CalculatorScreen> {
  // 1. Controller để lấy dữ liệu nhập vào từ 2 ô TextField
  final TextEditingController _num1Controller = TextEditingController();
  final TextEditingController _num2Controller = TextEditingController();

  // 2. Biến lưu trạng thái phép toán được chọn và kết quả
  String? _selectedOperator; // Lưu: '+', '-', '*', '/'
  String _result = '';       // Chuỗi hiển thị kết quả

  // Hàm định dạng số hiển thị (bỏ đuôi .0 nếu là số nguyên tròn)
  String _formatNumber(double num) {
    if (num % 1 == 0) {
      return num.toInt().toString();
    }
    return num.toString();
  }

  // 3. Hàm tính toán và cập nhật giao diện bằng setState
  void _calculate(String operator) {
    setState(() {
      _selectedOperator = operator;

      final double? num1 = double.tryParse(_num1Controller.text.trim());
      final double? num2 = double.tryParse(_num2Controller.text.trim());

      // Kiểm tra nếu chưa nhập đủ hoặc nhập sai định dạng số
      if (num1 == null || num2 == null) {
        _result = '';
        return;
      }

      // Xử lý từng phép toán
      switch (operator) {
        case '+':
          _result = _formatNumber(num1 + num2);
          break;
        case '-':
          _result = _formatNumber(num1 - num2);
          break;
        case '*':
          _result = _formatNumber(num1 * num2);
          break;
        case '/':
          if (num2 == 0) {
            _result = 'Không thể chia cho 0';
          } else {
            _result = _formatNumber(num1 / num2);
          }
          break;
      }
    });
  }

  @override
  void dispose() {
    _num1Controller.dispose();
    _num2Controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        title: const Text(
          'Thực hành 03',
          style: TextStyle(
            color: Colors.black87,
            fontSize: 16,
            fontWeight: FontWeight.w500,
          ),
        ),
        centerTitle: true,
        backgroundColor: Colors.white,
        elevation: 0,
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.symmetric(horizontal: 24.0, vertical: 12.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Ô nhập số thứ nhất
            TextField(
              controller: _num1Controller,
              keyboardType: const TextInputType.numberWithOptions(decimal: true),
              decoration: InputDecoration(
                contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 14),
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(14),
                  borderSide: BorderSide(color: Colors.grey.shade300),
                ),
                enabledBorder: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(14),
                  borderSide: BorderSide(color: Colors.grey.shade300),
                ),
              ),
              onChanged: (_) {
                if (_selectedOperator != null) _calculate(_selectedOperator!);
              },
            ),
            const SizedBox(height: 20),

            // Hàng chứa 4 nút phép toán: +, -, *, /
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                _buildOperatorButton('+', const Color(0xFFE25247)),
                _buildOperatorButton('-', const Color(0xFFE5B054)),
                _buildOperatorButton('*', const Color(0xFF6750A4)),
                _buildOperatorButton('/', const Color(0xFF1E293B)),
              ],
            ),
            const SizedBox(height: 20),

            // Ô nhập số thứ hai
            TextField(
              controller: _num2Controller,
              keyboardType: const TextInputType.numberWithOptions(decimal: true),
              decoration: InputDecoration(
                contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 14),
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(14),
                  borderSide: BorderSide(color: Colors.grey.shade300),
                ),
                enabledBorder: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(14),
                  borderSide: BorderSide(color: Colors.grey.shade300),
                ),
              ),
              onChanged: (_) {
                if (_selectedOperator != null) _calculate(_selectedOperator!);
              },
            ),
            const SizedBox(height: 28),

            // Hiển thị kết quả
            Text(
              _result.isEmpty ? 'Kết quả:' : 'Kết quả: $_result',
              style: const TextStyle(
                fontSize: 16,
                fontWeight: FontWeight.w600,
                color: Colors.black87,
              ),
            ),
          ],
        ),
      ),
    );
  }

  // Widget hỗ trợ vẽ từng nút bấm phép toán với hiệu ứng chọn viền
  Widget _buildOperatorButton(String op, Color color) {
    final bool isSelected = _selectedOperator == op;

    return GestureDetector(
      onTap: () => _calculate(op),
      child: Container(
        width: 65,
        height: 52,
        decoration: BoxDecoration(
          color: color,
          borderRadius: BorderRadius.circular(10),
          border: isSelected
              ? Border.all(color: Colors.black, width: 2.5) // Viền đen khi nút được chọn
              : null,
        ),
        alignment: Alignment.center,
        child: Text(
          op,
          style: const TextStyle(
            color: Colors.white,
            fontSize: 22,
            fontWeight: FontWeight.bold,
          ),
        ),
      ),
    );
  }
}