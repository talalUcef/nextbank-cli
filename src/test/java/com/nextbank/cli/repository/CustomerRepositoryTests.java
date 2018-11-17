package com.nextbank.cli.repository;

import com.nextbank.cli.domain.Customer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerRepositoryTests {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testFindCustomerByUsername() {
        final Customer customer = this.customerRepository.findCustomerByUsername("guido").orElse(null);
        Assert.assertNotNull(customer);
        Assert.assertEquals(customer.getName(), "Guido van Rossum");
    }
}