package com.example.eldercare.init;

import com.example.eldercare.entity.*;
import com.example.eldercare.repository.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataLoader implements ApplicationRunner {
    private final UserRepository userRepo;
    private final ElderRepository elderRepo;
    private final BedRepository bedRepo;
    private final FeeTypeRepository feeTypeRepo;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public DataLoader(UserRepository userRepo, ElderRepository elderRepo,
                     BedRepository bedRepo, FeeTypeRepository feeTypeRepo) {
        this.userRepo = userRepo;
        this.elderRepo = elderRepo;
        this.bedRepo = bedRepo;
        this.feeTypeRepo = feeTypeRepo;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 初始化测试用户
        if (userRepo.count() == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRealName("系统管理员");
            admin.setPhone("13800138000");
            admin.setRole("ADMIN");
            admin.setStatus(1);
            userRepo.save(admin);

            User nurse1 = new User();
            nurse1.setUsername("nurse01");
            nurse1.setPassword(passwordEncoder.encode("nurse123"));
            nurse1.setRealName("张护士");
            nurse1.setPhone("13900139001");
            nurse1.setRole("NURSE");
            nurse1.setStatus(1);
            userRepo.save(nurse1);

            User nurse2 = new User();
            nurse2.setUsername("nurse02");
            nurse2.setPassword(passwordEncoder.encode("nurse123"));
            nurse2.setRealName("李护士");
            nurse2.setPhone("13900139002");
            nurse2.setRole("NURSE");
            nurse2.setStatus(1);
            userRepo.save(nurse2);
        }

        // 初始化测试老人
        if (elderRepo.count() == 0) {
            Elder elder1 = new Elder();
            elder1.setName("王大爷");
            elder1.setGender(1);
            elder1.setBirthDate(LocalDate.of(1946, 5, 10));
            elder1.setIdCard("11010119460510001X");
            elder1.setPhone("13800000001");
            elder1.setAddress("北京市朝阳区");
            elder1.setNursingLevel("中度");
            elder1.setAdmissionDate(LocalDate.now());
            elder1.setEmergencyContact("王儿子");
            elder1.setEmergencyPhone("13900000001");
            elder1.setHealthStatus("高血压");
            elder1.setStatus(1);
            elderRepo.save(elder1);

            Elder elder2 = new Elder();
            elder2.setName("李大妈");
            elder2.setGender(2);
            elder2.setBirthDate(LocalDate.of(1942, 3, 15));
            elder2.setIdCard("110101194203150012");
            elder2.setPhone("13800000002");
            elder2.setAddress("北京市海淀区");
            elder2.setNursingLevel("轻度");
            elder2.setAdmissionDate(LocalDate.now());
            elder2.setEmergencyContact("李女儿");
            elder2.setEmergencyPhone("13900000002");
            elder2.setHealthStatus("糖尿病");
            elder2.setStatus(1);
            elderRepo.save(elder2);

            Elder elder3 = new Elder();
            elder3.setName("张阿姨");
            elder3.setGender(2);
            elder3.setBirthDate(LocalDate.of(1949, 8, 20));
            elder3.setIdCard("110101194908200013");
            elder3.setPhone("13800000003");
            elder3.setAddress("北京市西城区");
            elder3.setNursingLevel("重度");
            elder3.setAdmissionDate(LocalDate.now());
            elder3.setEmergencyContact("张儿子");
            elder3.setEmergencyPhone("13900000003");
            elder3.setHealthStatus("失能护理");
            elder3.setStatus(1);
            elderRepo.save(elder3);
        }

        // 初始化床位
        if (bedRepo.count() == 0) {
            Bed bed1 = new Bed();
            bed1.setBedNo("A-101-1");
            bed1.setRoomNo("101");
            bed1.setBuilding("A栋");
            bed1.setFloor(1);
            bed1.setType("单人间");
            bed1.setPrice(new BigDecimal("3000.00"));
            bed1.setStatus(0);
            bedRepo.save(bed1);

            Bed bed2 = new Bed();
            bed2.setBedNo("A-101-2");
            bed2.setRoomNo("102");
            bed2.setBuilding("A栋");
            bed2.setFloor(1);
            bed2.setType("单人间");
            bed2.setPrice(new BigDecimal("3000.00"));
            bed2.setStatus(0);
            bedRepo.save(bed2);

            Bed bed3 = new Bed();
            bed3.setBedNo("A-201-1");
            bed3.setRoomNo("201");
            bed3.setBuilding("A栋");
            bed3.setFloor(2);
            bed3.setType("双人间");
            bed3.setPrice(new BigDecimal("2000.00"));
            bed3.setStatus(0);
            bedRepo.save(bed3);
        }

        // 初始化费用类型
        if (feeTypeRepo.count() == 0) {
            FeeType feeType1 = new FeeType();
            feeType1.setFeeType("BED_FEE");
            feeType1.setFeeName("床位费");
            feeType1.setUnitPrice(new BigDecimal("3000.00"));
            feeType1.setUnit("月");
            feeType1.setStatus(1);
            feeTypeRepo.save(feeType1);

            FeeType feeType2 = new FeeType();
            feeType2.setFeeType("NURSING_FEE");
            feeType2.setFeeName("护理费");
            feeType2.setUnitPrice(new BigDecimal("1500.00"));
            feeType2.setUnit("月");
            feeType2.setStatus(1);
            feeTypeRepo.save(feeType2);

            FeeType feeType3 = new FeeType();
            feeType3.setFeeType("MEAL_FEE");
            feeType3.setFeeName("餐费");
            feeType3.setUnitPrice(new BigDecimal("600.00"));
            feeType3.setUnit("月");
            feeType3.setStatus(1);
            feeTypeRepo.save(feeType3);
        }
    }
}
