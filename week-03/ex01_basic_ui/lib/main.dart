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
      home: BasicUiScreen(),
    );
  }
}

class BasicUiScreen extends StatefulWidget {
  const BasicUiScreen({super.key});

  @override
  State<BasicUiScreen> createState() => _BasicUiScreenState();
}

class _BasicUiScreenState extends State<BasicUiScreen> {
  // Biến trạng thái: false là "Hello", true là "I'm Nguyen Van A"
  bool _isGreetingChanged = false;

  void _handleButtonPress() {
    setState(() {
      _isGreetingChanged = !_isGreetingChanged;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      body: SafeArea(
        child: SizedBox(
          width: double.infinity, // Kéo dãn chiều rộng toàn màn hình để căn giữa tuyệt đối
          child: Padding(
            padding: const EdgeInsets.symmetric(horizontal: 24.0, vertical: 48.0),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.spaceBetween, // Tách đều: trên - giữa - dưới
              crossAxisAlignment: CrossAxisAlignment.center,     // Căn giữa toàn bộ theo trục ngang
              children: [
                // 1. TextView tiêu đề phía trên
                const Text(
                  'My First App',
                  style: TextStyle(
                    fontSize: 22,
                    fontWeight: FontWeight.bold,
                    color: Colors.black,
                  ),
                  textAlign: TextAlign.center,
                ),

                // 2. TextView nội dung ở chính giữa màn hình
                Text(
                  _isGreetingChanged ? "I'm\nPhạm Nguyễn" : 'Hello',
                  style: const TextStyle(
                    fontSize: 18,
                    fontWeight: FontWeight.w500,
                    color: Colors.black87,
                    height: 1.3,
                  ),
                  textAlign: TextAlign.center,
                ),

                // 3. Nút bấm Say Hi! ở dưới cùng
                SizedBox(
                  width: 130,
                  height: 42,
                  child: ElevatedButton(
                    onPressed: _handleButtonPress,
                    style: ElevatedButton.styleFrom(
                      backgroundColor: const Color(0xFF1E60BD),
                      foregroundColor: Colors.white,
                      elevation: 0,
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(6),
                      ),
                    ),
                    child: const Text(
                      'Say Hi!',
                      style: TextStyle(
                        fontSize: 15,
                        fontWeight: FontWeight.w600,
                      ),
                    ),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}