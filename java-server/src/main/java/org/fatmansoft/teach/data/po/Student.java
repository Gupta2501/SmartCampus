package org.fatmansoft.teach.data.po;

import org.fatmansoft.teach.data.dto.StudentRequest;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Student学生表实体类 保存每个学生的信息，
 * Integer studentId 用户表 student 主键 student_id
 * Person person 关联到该用户所用的Person对象，账户所对应的人员信息 person_id 关联 person 表主键 person_id
 * String major 专业
 * String className 班级
 *
 */
@Entity
@Table(	name = "student",
        uniqueConstraints = {
        })
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer studentId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="person_id")
    private Person person;

    @Size(max = 20)
    private String major;

    @Size(max = 50)
    private String className;

    private @Version Long version;


    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Student() {
    }

    public Student (StudentRequest studentRequest) {
        person = new Person();
        person.setName(studentRequest.getName());
        person.setNum(studentRequest.getNum());
        person.setDept(studentRequest.getDept());
        person.setCard(studentRequest.getCard());
        person.setGender(studentRequest.getGender());
        person.setBirthday(studentRequest.getBirthday());
        person.setEmail(studentRequest.getEmail());
        person.setPhone(studentRequest.getPhone());
        person.setAddress(studentRequest.getAddress());
        person.setPersonId(studentRequest.getPersonId());
        setMajor(studentRequest.getMajor());
        setStudentId(studentRequest.getStudentId());
        setClassName(studentRequest.getClassName());
        person.setIntroduce(studentRequest.getIntroduce());

    }
}
