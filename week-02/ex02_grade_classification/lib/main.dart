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
      home: GradeClassificationScreen(),
    );
  }
}

class GradeClassificationScreen extends StatelessWidget {
  const GradeClassificationScreen({super.key});

  // 2. Viết hàm nhận vào điểm và trả về xếp loại bằng if / else
  String classifyGrade(double score) {
    if (score < 0 || score > 10) {
      return 'Không hợp lệ';
    } else if (score >= 8.0) {
      return 'Giỏi';
    } else if (score >= 6.5) {
      return 'Khá';
    } else if (score >= 5.0) {
      return 'Trung bình';
    } else {
      return 'Yếu';
    }
  }

  // 3. Hàm chọn màu chữ bằng if
  Color getTextColor(String grade) {
    if (grade == 'Giỏi') {
      return const Color(0xFF2E7D32);
    } else if (grade == 'Khá') {
      return const Color(0xFF1976D2);
    } else if (grade == 'Trung bình') {
      return const Color(0xFFE65100);
    } else if (grade == 'Yếu') {
      return const Color(0xFFC62828);
    } else {
      return const Color(0xFF757575);
    }
  }

  // Hàm chọn màu nền tag xếp loại bằng if
  Color getBgColor(String grade) {
    if (grade == 'Giỏi') {
      return const Color(0xFFE8F5E9);
    } else if (grade == 'Khá') {
      return const Color(0xFFE3F2FD);
    } else if (grade == 'Trung bình') {
      return const Color(0xFFFFF3E0);
    } else if (grade == 'Yếu') {
      return const Color(0xFFFFEBEE);
    } else {
      return const Color(0xFFEEEEEE);
    }
  }

  // Hàm định dạng số hiển thị (ví dụ 9.0 -> "9", 5.5 -> "5.5")
  String formatScore(double score) {
    if (score % 1 == 0) {
      return score.toInt().toString();
    }
    return score.toString();
  }

  @override
  Widget build(BuildContext context) {
    // 1. Khai báo biến điểm có giá trị 8.5
    const double mainScore = 8.5;
    final String mainGrade = classifyGrade(mainScore);

    // 4. Danh sách các điểm kiểm tra thêm: 9, 7, 5.5, 3 và 11
    final List<double> testScores = [9.0, 7.0, 5.5, 3.0, 11.0];

    return Scaffold(
      backgroundColor: const Color(0xFFF3F4F6),
      appBar: AppBar(
        title: const Text(
          'Xếp loại học lực',
          style: TextStyle(
            color: Colors.white,
            fontWeight: FontWeight.w600,
            fontSize: 18,
          ),
        ),
        backgroundColor: const Color(0xFF1E3A5F),
        elevation: 0,
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Thẻ kết quả điểm trung bình chính (8.5)
            Container(
              width: double.infinity,
              padding: const EdgeInsets.symmetric(vertical: 24.0, horizontal: 16.0),
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(16),
                boxShadow: [
                  BoxShadow(
                    color: Colors.black.withOpacity(0.04),
                    blurRadius: 6,
                    offset: const Offset(0, 2),
                  ),
                ],
              ),
              child: Column(
                children: [
                  const Text(
                    'Điểm trung bình',
                    style: TextStyle(
                      fontSize: 13,
                      color: Colors.grey,
                    ),
                  ),
                  const SizedBox(height: 8),
                  Text(
                    formatScore(mainScore),
                    style: const TextStyle(
                      fontSize: 40,
                      fontWeight: FontWeight.bold,
                      color: Color(0xFF1F2937),
                    ),
                  ),
                  const SizedBox(height: 16),
                  Container(
                    width: double.infinity,
                    padding: const EdgeInsets.symmetric(vertical: 12.0),
                    decoration: BoxDecoration(
                      color: getBgColor(mainGrade),
                      borderRadius: BorderRadius.circular(10),
                    ),
                    alignment: Alignment.center,
                    child: Text(
                      mainGrade,
                      style: TextStyle(
                        fontSize: 16,
                        fontWeight: FontWeight.bold,
                        color: getTextColor(mainGrade),
                      ),
                    ),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 24),

            // Tiêu đề phần kiểm tra thêm
            const Text(
              'KIỂM TRA THÊM',
              style: TextStyle(
                fontSize: 12,
                fontWeight: FontWeight.bold,
                color: Color(0xFF64748B),
                letterSpacing: 0.5,
              ),
            ),
            const SizedBox(height: 12),

            // Danh sách các dòng kiểm tra thêm bọc trong Container thẻ trắng
            Container(
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(14),
                boxShadow: [
                  BoxShadow(
                    color: Colors.black.withOpacity(0.04),
                    blurRadius: 6,
                    offset: const Offset(0, 2),
                  ),
                ],
              ),
              child: Column(
                children: [
                  for (int i = 0; i < testScores.length; i++) ...[
                    Padding(
                      padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 14.0),
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          Text(
                            'Điểm ${formatScore(testScores[i])}',
                            style: const TextStyle(
                              fontSize: 14,
                              color: Color(0xFF1F2937),
                            ),
                          ),
                          Container(
                            padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                            decoration: BoxDecoration(
                              color: getBgColor(classifyGrade(testScores[i])),
                              borderRadius: BorderRadius.circular(6),
                            ),
                            child: Text(
                              classifyGrade(testScores[i]),
                              style: TextStyle(
                                fontSize: 12,
                                fontWeight: FontWeight.w600,
                                color: getTextColor(classifyGrade(testScores[i])),
                              ),
                            ),
                          ),
                        ],
                      ),
                    ),
                    if (i < testScores.length - 1)
                      Container(
                        height: 1,
                        color: const Color(0xFFF1F5F9),
                      ),
                  ],
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}