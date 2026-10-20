import 'package:flutter/material.dart';

// 1. Khai báo Model Student với các thuộc tính nullable
class Student {
  final String name;
  final String? email;
  final String? phone;

  Student({
    required this.name,
    this.email,
    this.phone,
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
      home: StudentListScreen(),
    );
  }
}

class StudentListScreen extends StatelessWidget {
  const StudentListScreen({super.key});

  @override
  Widget build(BuildContext context) {
    // 2. Tạo danh sách 5 sinh viên theo mẫu đề bài
    final List<Student> students = [
      Student(
        name: 'Phạm Nguyễn',
        email: 'nguyenp6362@ut.edu.vn',
        phone: '0838337115',
      ),
      Student(
        name: 'Nguyễn Thái Khang',
        email: null,
        phone: null,
      ),
      Student(
        name: 'Trần Vân Phi',
        email: 'phitv.edu.vn',
        phone: null,
      ),
    ];

    // 5. Dùng for đếm số sinh viên thiếu email
    int missingEmailCount = 0;
    for (var student in students) {
      if (student.email == null) {
        missingEmailCount++;
      }
    }

    return Scaffold(
      backgroundColor: const Color(0xFFF3F4F6),
      appBar: AppBar(
        title: const Text(
          'Danh sách sinh viên',
          style: TextStyle(
            color: Colors.white,
            fontWeight: FontWeight.w600,
            fontSize: 18,
          ),
        ),
        backgroundColor: const Color(0xFF1E3A5F),
        elevation: 0,
      ),
      body: Column(
        children: [
          // Danh sách sinh viên
          Expanded(
            child: SingleChildScrollView(
              padding: const EdgeInsets.all(16.0),
              child: Column(
                children: [
                  // 3. Dùng vòng lặp for tạo các thẻ sinh viên
                  for (var student in students) ...[
                    Container(
                      width: double.infinity,
                      padding: const EdgeInsets.symmetric(
                        horizontal: 16.0,
                        vertical: 14.0,
                      ),
                      decoration: BoxDecoration(
                        color: Colors.white,
                        borderRadius: BorderRadius.circular(12),
                        boxShadow: [
                          BoxShadow(
                            color: Colors.black.withValues(alpha: 0.04),
                            blurRadius: 6,
                            offset: const Offset(0, 2),
                          ),
                        ],
                      ),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          // Tên sinh viên
                          Text(
                            student.name,
                            style: const TextStyle(
                              fontSize: 15,
                              fontWeight: FontWeight.bold,
                              color: Color(0xFF1F2937),
                            ),
                          ),
                          const SizedBox(height: 6),

                          // Email: Dùng toán tử ??
                          Row(
                            children: [
                              const Text(
                                'Email: ',
                                style: TextStyle(
                                  fontSize: 13,
                                  color: Colors.grey,
                                ),
                              ),
                              Text(
                                student.email ?? 'Chưa cập nhật',
                                style: TextStyle(
                                  fontSize: 13,
                                  color: student.email == null
                                      ? const Color(0xFFDC2626)
                                      : Colors.grey.shade700,
                                  fontStyle: student.email == null
                                      ? FontStyle.italic
                                      : FontStyle.normal,
                                ),
                              ),
                            ],
                          ),
                          const SizedBox(height: 4),

                          // SĐT: Dùng toán tử ??
                          Row(
                            children: [
                              const Text(
                                'SĐT: ',
                                style: TextStyle(
                                  fontSize: 13,
                                  color: Colors.grey,
                                ),
                              ),
                              Text(
                                student.phone ?? 'Chưa cập nhật',
                                style: TextStyle(
                                  fontSize: 13,
                                  color: student.phone == null
                                      ? const Color(0xFFDC2626)
                                      : Colors.grey.shade700,
                                  fontStyle: student.phone == null
                                      ? FontStyle.italic
                                      : FontStyle.normal,
                                ),
                              ),
                            ],
                          ),
                        ],
                      ),
                    ),
                    const SizedBox(height: 12),
                  ],
                ],
              ),
            ),
          ),

          // Thanh thống kê dưới đáy màn hình
          Container(
            padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 14),
            decoration: const BoxDecoration(
              color: Color(0xFFE5EDF7),
              border: Border(
                top: BorderSide(color: Color(0xFFD1DCEB)),
              ),
            ),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text(
                  'Tổng: ${students.length} sinh viên',
                  style: const TextStyle(
                    fontSize: 13,
                    fontWeight: FontWeight.w600,
                    color: Color(0xFF334155),
                  ),
                ),
                RichText(
                  text: TextSpan(
                    style: const TextStyle(
                      fontSize: 13,
                      color: Color(0xFF334155),
                    ),
                    children: [
                      const TextSpan(text: 'Thiếu email: '),
                      TextSpan(
                        text: '$missingEmailCount',
                        style: const TextStyle(
                          color: Color(0xFFDC2626),
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ],
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}