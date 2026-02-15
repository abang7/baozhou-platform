-- 检查是否有周日的课
SELECT course_date, weekday, source_weekday 
FROM class_schedules 
WHERE semester_id = (SELECT semester_id FROM semesters WHERE semester_name = '2025寒假班')
AND weekday = '周日'
ORDER BY course_date
LIMIT 10;
