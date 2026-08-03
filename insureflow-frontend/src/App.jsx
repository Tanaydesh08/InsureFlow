import { useEffect, useState } from 'react'

const API = import.meta.env.VITE_API_URL || 'http://localhost:8080'
const money = (amount) => new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR', maximumFractionDigits: 0 }).format(amount || 0)

function request(path, token, options = {}) {
  return fetch(`${API}${path}`, {
    ...options,
    headers: { 'Content-Type': 'application/json', ...(token ? { Authorization: `Bearer ${token}` } : {}), ...options.headers }
  }).then(async (response) => {
    const data = await response.json().catch(() => null)
    if (!response.ok) throw new Error(data?.message || data?.error || 'Something went wrong')
    return data
  })
}

const Card = ({ children, className = '' }) => <section className={`rounded-lg border border-slate-200 bg-white p-5 ${className}`}>{children}</section>
const Notice = ({ text, error }) => text && <p className={`mt-3 text-sm ${error ? 'text-red-600' : 'text-emerald-600'}`}>{text}</p>

function Auth({ onLogin }) {
  const [mode, setMode] = useState('login')
  const [form, setForm] = useState({ fullName: '', email: '', password: '' })
  const [message, setMessage] = useState('')
  const [error, setError] = useState('')
  const submit = async (event) => {
    event.preventDefault(); setError(''); setMessage('')
    try {
      const endpoint = mode === 'login' ? '/api/auth/login' : '/api/auth/register'
      const body = mode === 'login' ? { email: form.email, password: form.password } : form
      const data = await request(endpoint, null, { method: 'POST', body: JSON.stringify(body) })
      if (data.token) onLogin(data.token)
      else setMessage(data.message || 'Account created. You can now sign in.')
    } catch (err) { setError(err.message) }
  }
  return <main className="mx-auto flex min-h-screen max-w-5xl items-center px-4 py-10">
    <div className="grid w-full overflow-hidden rounded-xl border border-slate-200 bg-white shadow-sm md:grid-cols-2">
      <div className="bg-blue-700 p-8 text-white md:p-12"><p className="text-sm font-semibold tracking-wide text-blue-100">INSUREFLOW</p><h1 className="mt-8 text-3xl font-bold">Insurance made easier.</h1><p className="mt-4 text-blue-100">Keep your policies, premium payments and claims in one simple place.</p><div className="mt-10 border-t border-blue-500 pt-5 text-sm text-blue-100">For customers and administrators</div></div>
      <div className="p-8 md:p-12"><h2 className="text-2xl font-semibold">{mode === 'login' ? 'Welcome back' : 'Create an account'}</h2><p className="mt-1 text-sm text-slate-500">{mode === 'login' ? 'Sign in to manage your insurance.' : 'Register as a customer to get started.'}</p>
        <form className="mt-7 space-y-4" onSubmit={submit}>
          {mode === 'register' && <label className="block text-sm font-medium">Full name<input required value={form.fullName} onChange={e => setForm({ ...form, fullName: e.target.value })} className="mt-1" /></label>}
          <label className="block text-sm font-medium">Email<input required type="email" value={form.email} onChange={e => setForm({ ...form, email: e.target.value })} className="mt-1" /></label>
          <label className="block text-sm font-medium">Password<input required minLength="6" type="password" value={form.password} onChange={e => setForm({ ...form, password: e.target.value })} className="mt-1" /></label>
          <button className="w-full bg-blue-700 text-white hover:bg-blue-800">{mode === 'login' ? 'Sign in' : 'Create account'}</button>
        </form><Notice text={message} /><Notice text={error} error />
        <button onClick={() => { setMode(mode === 'login' ? 'register' : 'login'); setError(''); setMessage('') }} className="mt-5 w-full border border-slate-300 text-slate-700 hover:bg-slate-50">{mode === 'login' ? 'New here? Create an account' : 'Already have an account? Sign in'}</button>
      </div>
    </div>
  </main>
}

function Dashboard({ token, logout }) {
  const [tab, setTab] = useState('overview'), [policies, setPolicies] = useState([]), [claims, setClaims] = useState([]), [payments, setPayments] = useState([]), [types, setTypes] = useState([]), [notice, setNotice] = useState(''), [error, setError] = useState('')
  const load = async () => { try { const [p, c, pay, t] = await Promise.all([request('/api/policies/my', token), request('/api/claims/my', token), request('/api/payments/my', token), request('/api/policy-types', token)]); setPolicies(p); setClaims(c); setPayments(pay); setTypes(t) } catch (err) { setError(err.message) } }
  useEffect(() => { load() }, [])
  const action = async (path, method, body, message) => { setError(''); setNotice(''); try { await request(path, token, { method, body: body ? JSON.stringify(body) : undefined }); setNotice(message); load() } catch (err) { setError(err.message) } }
  const active = policies.filter(p => p.status === 'ACTIVE').length
  return <div className="min-h-screen"><header className="border-b border-slate-200 bg-white"><div className="mx-auto flex max-w-6xl items-center justify-between px-4 py-4"><div><span className="font-bold text-blue-700">InsureFlow</span><span className="ml-2 text-sm text-slate-500">Customer portal</span></div><button onClick={logout} className="border border-slate-300 text-slate-700 hover:bg-slate-50">Sign out</button></div></header>
    <div className="mx-auto max-w-6xl px-4 py-6"><nav className="mb-6 flex gap-2 overflow-x-auto border-b border-slate-200">{['overview', 'policies', 'payments', 'claims'].map(item => <button key={item} onClick={() => setTab(item)} className={`rounded-t px-3 py-2 capitalize ${tab === item ? 'border-b-2 border-blue-700 text-blue-700' : 'text-slate-500 hover:text-slate-800'}`}>{item}</button>)}</nav><Notice text={notice} /><Notice text={error} error />
      {tab === 'overview' && <><h1 className="text-2xl font-semibold">My dashboard</h1><p className="mt-1 text-sm text-slate-500">A quick view of your insurance activity.</p><div className="mt-5 grid gap-4 sm:grid-cols-3"><Card><p className="text-sm text-slate-500">Active policies</p><p className="mt-2 text-2xl font-semibold">{active}</p></Card><Card><p className="text-sm text-slate-500">Claims submitted</p><p className="mt-2 text-2xl font-semibold">{claims.length}</p></Card><Card><p className="text-sm text-slate-500">Payments made</p><p className="mt-2 text-2xl font-semibold">{payments.length}</p></Card></div><Card className="mt-5"><h2 className="font-semibold">Need help?</h2><p className="mt-1 text-sm text-slate-500">Choose a policy plan, pay an active policy premium, or submit a claim from the tabs above.</p></Card></>}
      {tab === 'policies' && <Policies policies={policies} types={types} buy={(id) => action('/api/policies', 'POST', { policyTypeId: Number(id) }, 'Policy purchased successfully.')} cancel={(id) => action(`/api/policies/${id}`, 'DELETE', null, 'Policy cancelled successfully.')} />}
      {tab === 'payments' && <Payments policies={policies} payments={payments} pay={(body) => action('/api/payments', 'POST', body, 'Payment recorded successfully.')} />}
      {tab === 'claims' && <Claims policies={policies} claims={claims} submit={(body) => action('/api/claims', 'POST', body, 'Claim submitted successfully.')} />}
    </div></div>
}

function Policies({ policies, types, buy, cancel }) { const [selected, setSelected] = useState(''); return <><div className="flex flex-wrap items-end justify-between gap-3"><div><h1 className="text-2xl font-semibold">My policies</h1><p className="mt-1 text-sm text-slate-500">View your existing policies or purchase a new one.</p></div><div className="flex gap-2"><select value={selected} onChange={e => setSelected(e.target.value)}><option value="">Select a plan</option>{types.filter(t => t.active).map(t => <option key={t.id} value={t.id}>{t.name} — {money(t.premiumAmount)}</option>)}</select><button disabled={!selected} onClick={() => buy(selected)} className="bg-blue-700 text-white disabled:bg-slate-300">Buy policy</button></div></div><div className="mt-5 space-y-3">{policies.length ? policies.map(p => <Card key={p.id} className="flex flex-wrap items-center justify-between gap-3"><div><p className="font-semibold">{p.policyType}</p><p className="mt-1 text-sm text-slate-500">{p.policyNumber} · Ends {p.endDate}</p></div><div className="flex items-center gap-4"><span className="text-sm font-medium text-slate-700">{money(p.premiumAmount)}</span><span className="rounded bg-emerald-50 px-2 py-1 text-xs font-medium text-emerald-700">{p.status}</span>{p.status === 'ACTIVE' && <button onClick={() => cancel(p.id)} className="border border-red-200 text-red-600 hover:bg-red-50">Cancel</button>}</div></Card>) : <Card><p className="text-sm text-slate-500">No policies yet. Select a plan above to purchase one.</p></Card>}</div></> }

function Payments({ policies, payments, pay }) { const [form, setForm] = useState({ policyId: '', paymentMethod: 'UPI' }); return <><h1 className="text-2xl font-semibold">Premium payments</h1><div className="mt-5 grid gap-5 md:grid-cols-3"><Card><h2 className="font-semibold">Make a payment</h2><div className="mt-4 space-y-3"><select value={form.policyId} onChange={e => setForm({ ...form, policyId: e.target.value })}><option value="">Choose policy</option>{policies.filter(p => p.status === 'ACTIVE').map(p => <option key={p.id} value={p.id}>{p.policyNumber}</option>)}</select><select value={form.paymentMethod} onChange={e => setForm({ ...form, paymentMethod: e.target.value })}><option>UPI</option><option>CARD</option><option>NET_BANKING</option><option>CASH</option></select><button disabled={!form.policyId} onClick={() => pay({ ...form, policyId: Number(form.policyId) })} className="w-full bg-blue-700 text-white disabled:bg-slate-300">Pay premium</button></div></Card><Card className="md:col-span-2"><h2 className="font-semibold">Payment history</h2><div className="mt-3 divide-y divide-slate-100">{payments.length ? payments.map(p => <div key={p.id} className="flex justify-between py-3 text-sm"><div><p>{p.policyNumber}</p><p className="text-slate-500">{p.paymentDate?.slice(0, 10)} · {p.paymentMethod}</p></div><div className="text-right"><p>{money(p.amount)}</p><p className="text-emerald-600">{p.paymentStatus}</p></div></div>) : <p className="py-4 text-sm text-slate-500">No payments recorded.</p>}</div></Card></div></> }

function Claims({ policies, claims, submit }) { const [form, setForm] = useState({ policyId: '', claimAmount: '', reason: '', description: '' }); const update = e => setForm({ ...form, [e.target.name]: e.target.value }); return <><h1 className="text-2xl font-semibold">Claims</h1><div className="mt-5 grid gap-5 md:grid-cols-3"><Card><h2 className="font-semibold">Submit a claim</h2><div className="mt-4 space-y-3"><select name="policyId" value={form.policyId} onChange={update}><option value="">Choose policy</option>{policies.filter(p => p.status === 'ACTIVE').map(p => <option key={p.id} value={p.id}>{p.policyNumber}</option>)}</select><input name="claimAmount" type="number" min="1" placeholder="Claim amount" value={form.claimAmount} onChange={update} /><input name="reason" placeholder="Reason" value={form.reason} onChange={update} /><textarea name="description" placeholder="Brief description" rows="3" value={form.description} onChange={update} /><button disabled={!form.policyId || !form.claimAmount || !form.reason || !form.description} onClick={() => submit({ ...form, policyId: Number(form.policyId), claimAmount: Number(form.claimAmount) })} className="w-full bg-blue-700 text-white disabled:bg-slate-300">Submit claim</button></div></Card><Card className="md:col-span-2"><h2 className="font-semibold">Claim history</h2><div className="mt-3 divide-y divide-slate-100">{claims.length ? claims.map(c => <div key={c.id} className="flex justify-between gap-3 py-3 text-sm"><div><p className="font-medium">{c.reason}</p><p className="text-slate-500">{c.policyNumber} · {c.claimDate?.slice(0, 10)}</p></div><div className="text-right"><p>{money(c.claimAmount)}</p><p className="text-blue-700">{c.status}</p></div></div>) : <p className="py-4 text-sm text-slate-500">No claims submitted.</p>}</div></Card></div></> }

export default function App() { const [token, setToken] = useState(() => localStorage.getItem('insureflow_token')); const login = value => { localStorage.setItem('insureflow_token', value); setToken(value) }; const logout = () => { localStorage.removeItem('insureflow_token'); setToken(null) }; return token ? <Dashboard token={token} logout={logout} /> : <Auth onLogin={login} /> }
