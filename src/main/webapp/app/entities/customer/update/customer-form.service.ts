import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICustomer, NewCustomer } from '../customer.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICustomer for edit and NewCustomerFormGroupInput for create.
 */
type CustomerFormGroupInput = ICustomer | PartialWithRequiredKeyOf<NewCustomer>;

type CustomerFormDefaults = Pick<NewCustomer, 'id' | 'isActive'>;

type CustomerFormGroupContent = {
  id: FormControl<ICustomer['id'] | NewCustomer['id']>;
  code: FormControl<ICustomer['code']>;
  name: FormControl<ICustomer['name']>;
  creditLimit: FormControl<ICustomer['creditLimit']>;
  rating: FormControl<ICustomer['rating']>;
  isActive: FormControl<ICustomer['isActive']>;
  phone: FormControl<ICustomer['phone']>;
  addressLine1: FormControl<ICustomer['addressLine1']>;
  addressLine2: FormControl<ICustomer['addressLine2']>;
  city: FormControl<ICustomer['city']>;
  country: FormControl<ICustomer['country']>;
  email: FormControl<ICustomer['email']>;
};

export type CustomerFormGroup = FormGroup<CustomerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CustomerFormService {
  createCustomerFormGroup(customer: CustomerFormGroupInput = { id: null }): CustomerFormGroup {
    const customerRawValue = {
      ...this.getFormDefaults(),
      ...customer,
    };
    return new FormGroup<CustomerFormGroupContent>({
      id: new FormControl(
        { value: customerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      code: new FormControl(customerRawValue.code),
      name: new FormControl(customerRawValue.name, {
        validators: [Validators.required],
      }),
      creditLimit: new FormControl(customerRawValue.creditLimit),
      rating: new FormControl(customerRawValue.rating),
      isActive: new FormControl(customerRawValue.isActive),
      phone: new FormControl(customerRawValue.phone, {
        validators: [Validators.required],
      }),
      addressLine1: new FormControl(customerRawValue.addressLine1),
      addressLine2: new FormControl(customerRawValue.addressLine2),
      city: new FormControl(customerRawValue.city, {
        validators: [Validators.required],
      }),
      country: new FormControl(customerRawValue.country),
      email: new FormControl(customerRawValue.email),
    });
  }

  getCustomer(form: CustomerFormGroup): ICustomer | NewCustomer {
    return form.getRawValue() as ICustomer | NewCustomer;
  }

  resetForm(form: CustomerFormGroup, customer: CustomerFormGroupInput): void {
    const customerRawValue = { ...this.getFormDefaults(), ...customer };
    form.reset(
      {
        ...customerRawValue,
        id: { value: customerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CustomerFormDefaults {
    return {
      id: null,
      isActive: false,
    };
  }
}
