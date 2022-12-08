export interface ICustomer {
  id: number;
  code?: string | null;
  name?: string | null;
  creditLimit?: number | null;
  rating?: number | null;
  isActive?: boolean | null;
  phone?: string | null;
  addressLine1?: string | null;
  addressLine2?: string | null;
  city?: string | null;
  country?: string | null;
  email?: string | null;
}

export type NewCustomer = Omit<ICustomer, 'id'> & { id: null };
