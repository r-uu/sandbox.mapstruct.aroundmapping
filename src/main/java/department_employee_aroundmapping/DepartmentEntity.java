package department_employee_aroundmapping;

import static lombok.AccessLevel.PROTECTED;

import department_employee_aroundmapping.MapStructMapper.Default;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@NoArgsConstructor(access = PROTECTED) // generate no args constructor for jpa, mapstruct, ...
//@Accessors(fluent = true) // mapstruct does not seem to support fluent accessors
@Getter()
@ToString
@EqualsAndHashCode
public class DepartmentEntity
{
	/** can not be modified from outside, not final because otherwise there has to be a constructor with setId-parameter */
	private Long id;

	/** mutable non-null */
	@NonNull @Setter private String name;

	/** mutable */
	@Setter private String description;

	/**
	 * let this be used by mapstruct (@Default, @ObjectFactory) and make sure to manually call required args constructor
	 * <p>random value will be assigned to {@link #id}
	 * @param department incoming DTO to be used for construction of instance
	 * @param context incoming context to properly handling cyclic dependencies
	 */
//	@Default // necessary make sure mapstruct does not use no-args-constructor
	public DepartmentEntity(@NonNull DepartmentDTO department, @NonNull MapStructMapper.CycleTracking context)
	{
		this(department.getName());
		setId(new Random().nextLong());
		log.debug("{}, context {}", this, context);
	}

	void beforeMapping(@NonNull DepartmentDTO dto)
	{
		log.debug("entity {}, dto {}", this, dto);
		// set fields that can not be modified from outside
		setId(dto.getId());
	}

	void afterMapping(@NonNull DepartmentDTO dto)
	{
		log.debug("entity {}, dto {}", this, dto);
	}

	private void setId(@NonNull Long id) { this.id = id; }
}