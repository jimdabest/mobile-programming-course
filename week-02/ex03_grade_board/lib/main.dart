import 'package:flutter/material.dart';

// 1. Khai báo class Student gồm name và score (nullable)
class Student {
  final String name;
  final double? score;

  Student({
    required this.name,
    this.score,
  });
}

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return const MaterialApp(
      debugShowCheckedModeBanner: false,
      home: GradeBoardScreen(),
    );
  }
}

class GradeBoardScreen extends StatelessWidget {
  const GradeBoardScreen({super.key});

  // 2. Hàm xếp loại (kết hợp logic Bài 2 và kiểm tra null)
  String classifyGrade(double? score) {
    if (score == null) {
      return 'Chưa có điểm';
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

  // Chọn màu chữ cho xếp loại
  Color getGradeColor(double? score) {
    if (score == null) {
      return const Color(0xFF9E9E9E); // Xám
    } else if (score >= 8.0) {
      return const Color(0xFF2E7D32); // Xanh lá
    } else if (score >= 6.5) {
      return const Color(0xFF1976D2); // Xanh dương
    } else if (score >= 5.0) {
      return const Color(0xFFE65100); // Cam
    } else {
      return const Color(0xFFC62828); // Đỏ
    }
  }

  @override
  Widget build(BuildContext context) {
    // Dữ liệu mẫu đề bài yêu cầu
    final List<Student> students = [
      Student(name: 'Phạm Nguyễn', score: 9.0),
      Student(name: 'Trần Vân Phi', score: 7.2),
      Student(name: 'Nguyễn Thái Khang', score: null),
      Student(name: 'Nguyễn Vũ Thuân', score: 4.5),
      Student(name: 'Lê Anh Nui', score: 5.8),
    ];

    // 3. Dùng for + if để tính các chỉ số thống kê
    double totalScore = 0.0;
    int scoredCount = 0;
    int goodCount = 0;
    int nullCount = 0;
    double maxScore = 0.0;

    for (var student in students) {
      if (student.score != null) {
        double s = student.score!;
        totalScore += s;
        scoredCount++;

        // Tìm điểm cao nhất
        if (s > maxScore) {
          maxScore = s;
        }

        // Đếm học sinh giỏi
        if (s >= 8.0) {
          goodCount++;
        }
      } else {
        // Đếm học sinh chưa có điểm
        nullCount++;
      }
    }

    // Tính điểm trung bình (bỏ qua người chưa có điểm)
    double averageScore = scoredCount > 0 ? (totalScore / scoredCount) : 0.0;

    return Scaffold(
      backgroundColor: const Color(0xFFF3F4F6),
      appBar: AppBar(
        title: const Text(
          'Bảng điểm lớp',
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
            // Thẻ thống kê tổng quan màu xanh navy ở trên cùng
            Container(
              width: double.infinity,
              padding: const EdgeInsets.all(20.0),
              decoration: BoxDecoration(
                color: const Color(0xFF1E3A5F),
                borderRadius: BorderRadius.circular(16),
              ),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  const Text(
                    'Điểm trung bình lớp',
                    style: TextStyle(
                      color: Color(0xFF94A3B8),
                      fontSize: 13,
                    ),
                  ),
                  const SizedBox(height: 6),
                  Text(
                    averageScore.toStringAsFixed(2),
                    style: const TextStyle(
                      color: Colors.white,
                      fontSize: 36,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  const SizedBox(height: 18),
                  // Hàng thống kê 3 cột phụ bên dưới
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      // Cột 1: Giỏi
                      Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          const Text(
                            'Giỏi',
                            style: TextStyle(color: Color(0xFF94A3B8), fontSize: 11),
                          ),
                          const SizedBox(height: 4),
                          Text(
                            '$goodCount',
                            style: const TextStyle(
                              color: Colors.white,
                              fontSize: 16,
                              fontWeight: FontWeight.bold,
                            ),
                          ),
                        ],
                      ),
                      // Cột 2: Chưa có điểm
                      Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          const Text(
                            'Chưa có điểm',
                            style: TextStyle(color: Color(0xFF94A3B8), fontSize: 11),
                          ),
                          const SizedBox(height: 4),
                          Text(
                            '$nullCount',
                            style: const TextStyle(
                              color: Colors.white,
                              fontSize: 16,
                              fontWeight: FontWeight.bold,
                            ),
                          ),
                        ],
                      ),
                      // Cột 3: Cao nhất
                      Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          const Text(
                            'Cao nhất',
                            style: TextStyle(color: Color(0xFF94A3B8), fontSize: 11),
                          ),
                          const SizedBox(height: 4),
                          Text(
                            maxScore.toString(),
                            style: const TextStyle(
                              color: Colors.white,
                              fontSize: 16,
                              fontWeight: FontWeight.bold,
                            ),
                          ),
                        ],
                      ),
                    ],
                  ),
                ],
              ),
            ),
            const SizedBox(height: 24),

            // Tiêu đề danh sách
            Text(
              'DANH SÁCH • ${students.length} SINH VIÊN',
              style: const TextStyle(
                fontSize: 12,
                fontWeight: FontWeight.bold,
                color: Color(0xFF64748B),
                letterSpacing: 0.5,
              ),
            ),
            const SizedBox(height: 12),

            // 2. Dùng for hiển thị danh sách từng sinh viên
            for (var student in students) ...[
              Container(
                width: double.infinity,
                padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 14.0),
                decoration: BoxDecoration(
                  color: Colors.white,
                  borderRadius: BorderRadius.circular(12),
                  boxShadow: [
                    BoxShadow(
                      color: Colors.black.withValues(alpha: 0.03),
                      blurRadius: 4,
                      offset: const Offset(0, 1),
                    ),
                  ],
                ),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    // Tên và Xếp loại
                    Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          student.name,
                          style: const TextStyle(
                            fontSize: 15,
                            fontWeight: FontWeight.bold,
                            color: Color(0xFF1F2937),
                          ),
                        ),
                        const SizedBox(height: 4),
                        Text(
                          classifyGrade(student.score),
                          style: TextStyle(
                            fontSize: 12,
                            fontWeight: FontWeight.w500,
                            color: getGradeColor(student.score),
                          ),
                        ),
                      ],
                    ),
                    // Điểm số hiển thị góc phải (nếu null thì hiện dấu gạch ngang '-')
                    Text(
                      student.score != null ? student.score.toString() : '—',
                      style: const TextStyle(
                        fontSize: 18,
                        fontWeight: FontWeight.bold,
                        color: Color(0xFF1F2937),
                      ),
                    ),
                  ],
                ),
              ),
              const SizedBox(height: 10),
            ],
          ],
        ),
      ),
    );
  }
}