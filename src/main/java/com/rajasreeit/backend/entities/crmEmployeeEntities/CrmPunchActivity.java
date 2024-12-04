package com.rajasreeit.backend.entities.crmEmployeeEntities;

import com.rajasreeit.backend.entities.CrmEmployee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CrmPunchActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private CrmEmployee crmEmployee;

    private String date;

    private String timeOfPunchIn;
    private String timeOfPunchOut;

    @Lob
    private byte[] punchInImage;  // Store punch-in image as a LOB
    @Lob
    private byte[] punchOutImage;  // Store punch-out image as a LOB

}
