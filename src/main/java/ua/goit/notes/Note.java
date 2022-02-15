package ua.goit.notes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import ua.goit.base.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notes")
public class Note implements BaseEntity<UUID> {

    private static final long serialVersionUID = 6174182882601741785L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "serial")
    private UUID id;

    @Column(name = "name", length = 100)
    @NotBlank
    private String name;

    @Column(name = "text", length = 10000)
    @NotBlank
    private String text;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private AccssesType accssesType;
}