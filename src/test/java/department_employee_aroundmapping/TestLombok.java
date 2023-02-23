package department_employee_aroundmapping;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class TestLombok
{
	@Test void emptyDepartmentDTO()
	{
		DepartmentDTO department = new DepartmentDTO();

		assertThat(department.name(), is(nullValue()));
	}

	@Test void emptyEmployeeDTO()
	{
		EmployeeDTO employeeDTO = new EmployeeDTO();

		assertThat(employeeDTO.name(), is(nullValue()));
	}

	@Test void emptyDepartmentEntity()
	{
		DepartmentEntity departmentEntity = new DepartmentEntity();

		assertThat(departmentEntity.name(), is(nullValue()));
	}

	@Test void emptyEmployeeEntity()
	{
		EmployeeEntity employeeEntity = new EmployeeEntity();

		assertThat(employeeEntity.name(), is(nullValue()));
	}

	@Test void invalidNameDepartmentDTO()
	{
		String name = null;

		assertThrows(NullPointerException.class, () -> new DepartmentDTO(name));
	}

	@Test void invalidNameEmployeeDTO()
	{
		String name = null;
		DepartmentDTO department = new DepartmentDTO("name");

		assertThrows(NullPointerException.class, () -> new EmployeeDTO(name, department));
	}

	@Test void invalidDepartmentEmployeeDTO()
	{
		String name = "name";
		DepartmentDTO department = null;

		assertThrows(NullPointerException.class, () -> new EmployeeDTO(name, department));
	}

	@Test void invalidNameDepartmentEntity()
	{
		String name = null;

		assertThrows(NullPointerException.class, () -> new DepartmentEntity(name));
	}

	@Test void invalidNameEmployeeEntity()
	{
		String name = null;
		DepartmentEntity department = new DepartmentEntity("name");

		assertThrows(NullPointerException.class, () -> new EmployeeEntity(name, department));
	}

	@Test void validDepartmentDTO()
	{
		String name = "name";
		DepartmentDTO departmentDTO  = new DepartmentDTO(name);

		assertThat(departmentDTO.name(), is(name));
	}

	@Test void validEmployeeInDepartmentDTO()
	{
		String name = "name";
		DepartmentDTO department = new DepartmentDTO(name);
		EmployeeDTO employee  = new EmployeeDTO(name, department);

		assertThat(employee.name()      , is(name));
		assertThat(employee.department(), is(department));
	}
}